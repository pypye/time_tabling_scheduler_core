package entities;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import core.constraints.defaults.TwoClassOverlap;
import core.constraints.distributions.*;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.courses.Class;
import entities.courses.Course;
import entities.rooms.Room;
import utils.StringFormatter;

import java.util.*;

public class Problem {
    private String name;
    private int nrDays;
    private int slotsPerDay;
    private int nrWeeks;

    private Optimization optimization;

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Time> times = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();
    private Map<String, Class> classes = new LinkedHashMap<>();
    private Map<String, Student> students = new HashMap<>();
    private List<Distribution> distributions = new ArrayList<>();

    private final Map<Placement, Literal> placements = new HashMap<>(); // all possible placements

    private final Map<Placement, ArrayList<Literal>> placementConflicts = new HashMap<>(); // placement conflict for 2 classes not overlap

    private final Map<Placement, ArrayList<Class>> placementClasses = new HashMap<>(); // placement x has list of classes that can be placed in x

    private final ArrayList<LinearExpr> softDistributionExpr = new ArrayList<>(); // soft distribution constraint

    public Problem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNrDays() {
        return nrDays;
    }

    public void setNrDays(int nrDays) {
        this.nrDays = nrDays;
    }

    public int getSlotsPerDay() {
        return slotsPerDay;
    }

    public void setSlotsPerDay(int slotsPerDay) {
        this.slotsPerDay = slotsPerDay;
    }

    public int getNrWeeks() {
        return nrWeeks;
    }

    public void setNrWeeks(int nrWeeks) {
        this.nrWeeks = nrWeeks;
    }

    public Optimization getOptimization() {
        return optimization;
    }

    public void setOptimization(Optimization optimization) {
        this.optimization = optimization;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Map<String, Time> getTimes() {
        return times;
    }

    public void setTimes(Map<String, Time> times) {
        this.times = times;
    }

    public Map<String, Course> getCourses() {
        return courses;
    }

    public void setCourses(Map<String, Course> courses) {
        this.courses = courses;
    }

    public Map<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Class> classes) {
        this.classes = classes;
    }

    public Map<String, Student> getStudents() {
        return students;
    }

    public void setStudents(Map<String, Student> students) {
        this.students = students;
    }

    public List<Distribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<Distribution> distributions) {
        this.distributions = distributions;
    }

    public Map<Placement, ArrayList<Literal>> getPlacementConflicts() {
        return placementConflicts;
    }

    public Map<Placement, Literal> getPlacements() {
        return placements;
    }

    public Map<Placement, ArrayList<Class>> getPlacementClasses() {
        return placementClasses;
    }

    public ArrayList<LinearExpr> getSoftDistributionExpr() {
        return softDistributionExpr;
    }

    public void makePlacementLiteralFromPlacement() {
        for (Placement p : placements.keySet()) {
            Literal l = Factory.getModel().newBoolVar("p_" + p);
            placements.put(p, l);
        }
    }

    public void findPlacementOverlapConflict() {
        ArrayList<Placement> placementList = new ArrayList<>(placements.keySet());
        for (int i = 0; i < placementList.size(); i++) {
            Placement p = placementList.get(i);
            if (p.getRoom() == null) {
                continue;
            }
            for (int j = i + 1; j < placementList.size(); j++) {
                Placement q = placementList.get(j);
                if (q.getRoom() == null) {
                    continue;
                }
                if (TwoClassOverlap.compare(p.getRoom(), q.getRoom(), p.getTime(), q.getTime())) {
                    Factory.getModel().addBoolOr(new Literal[]{placements.get(p).not(), placements.get(q).not()});
                }
            }
        }
    }

    public void resolvePlacementOverlapConflict() {
        for (Placement p : placements.keySet()) {
            if (placementConflicts.containsKey(p)) {
                ArrayList<Literal> conflicts = placementConflicts.get(p);
                Factory.getModel().addAtMostOne(conflicts.toArray(new Literal[0]));
            }
        }
    }

    public void preRemovePlacementDistributionConflict() {
        for (Distribution d : distributions) {
            if (d.isRequired()) {
                for (int i = 0; i < d.getClassList().size(); i++) {
                    for (int j = i + 1; j < d.getClassList().size(); j++) {
                        Class c1 = classes.get(d.getClassList().get(i));
                        Class c2 = classes.get(d.getClassList().get(j));
                        c1.removeDistributionConflict(d.getType(), c2);
                        c2.removeDistributionConflict(d.getType(), c1);
                    }
                }
            }
        }
    }

    public void resolvePlacementDistributionConflict() {
        int count = 0;
        for (Distribution d : distributions) {
            count++;
            System.out.println("Distribution " + count + " of " + distributions.size() + " : " + d);
            switch (d.getType()) {
                case "DifferentDays", "DifferentWeeks", "DifferentTime", "NotOverlap", "Overlap", "SameDays",
                     "SameStart", "SameTime", "SameWeeks":
                    this.distributionConflictWithTime(d);
                    break;
                case "DifferentRoom", "SameRoom":
                    this.distributionConflictWithRoom(d);
                    break;
                case "SameAttendees":
                    this.distributionConflictWithPlacement(d);
                    break;
                default:
                    break;
            }
            if (MinGap.isMinGap(d.getType())) {
                this.distributionConflictWithTime(d);
            }
            if (WorkDay.isWorkDayType(d.getType())) {
                this.distributionConflictWithTime(d);
            }
        }
    }

    public void resolveStudentClassLimit() {
        for (Class c : classes.values()) {
            ArrayList<Literal> classLit = new ArrayList<>();
            for (Student s : students.values()) {
                if (!s.getClasses().containsKey(c)) {
                    continue;
                }
                Literal scl = s.getClasses().get(c);
                classLit.add(scl);
            }
            if (!classLit.isEmpty()) {
                LinearExpr expr = LinearExpr.sum(classLit.toArray(new Literal[0]));
                Factory.getModel().addLessOrEqual(expr, c.getLimit());
            }
        }
    }

    public void computePenaltyObjective() {
        ArrayList<Literal> penalties = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        for (Class c : classes.values()) {
            for (Penalty<Room> pr : c.getRoomList()) {
                Room r = pr.getEntity();
                for (Penalty<Time> pt : c.getTimeList()) {
                    Time t = pt.getEntity();
                    Placement p = new Placement(r, t);
                    if (c.getPlacements().containsKey(p)) {
                        penalties.add(c.getPlacements().get(p));
                        weights.add(
                            Factory.getProblem().getOptimization().getRoom() * pr.getPenalty() + Factory.getProblem().getOptimization().getTime() * pt.getPenalty()
                        );
                    }
                }
            }
        }

        LinearExpr expr = LinearExpr.weightedSum(penalties.toArray(new Literal[0]), weights.stream().mapToLong(i -> i).toArray());
        LinearExprBuilder builder = LinearExpr.newBuilder().add(expr);
        for (LinearExpr e : softDistributionExpr) {
            builder.add(e);
        }
        Factory.getModel().minimize(builder.build());
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }


    private void distributionConflictWithPlacement(Distribution d) {
        Map<Placement, Literal> pLit = new HashMap<>();
        Map<Placement, ArrayList<Literal>> pLitConflict = new HashMap<>();

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Placement p : c.getPlacements().keySet()) {
                if (!pLit.containsKey(p)) {
                    pLit.put(p, Factory.getModel().newBoolVar("xp_" + p));
                }
            }
        }

        for (Placement p : pLit.keySet()) {
            for (Placement q : pLit.keySet()) {
                if (p.equals(q)) {
                    continue;
                }
                this.resolveDistributionConflictWithPlacement(d.getType(), p, q, pLit.get(p), pLit.get(q), d.isRequired(), d.getPenalty());
            }
        }

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Placement p : c.getPlacements().keySet()) {
                Literal cpl = c.getPlacements().get(p);
                Literal cpld = pLit.get(p);
                Factory.getModel().addImplication(cpl, cpld);
                if (!pLitConflict.containsKey(p)) {
                    pLitConflict.put(p, new ArrayList<>());
                }
            }
        }

        if (d.isRequired()) {
            for (Placement p : pLitConflict.keySet()) {
                Factory.getModel().addAtMostOne(pLitConflict.get(p).toArray(new Literal[0]));
            }
        } else {
            for (Placement p : pLitConflict.keySet()) {
                addSoftDistributionConstraint(d, pLitConflict.get(p));
            }
        }
    }

    private void distributionConflictWithTime(Distribution d) {
        Map<Time, Literal> pTime = new HashMap<>();
        Map<Time, ArrayList<Literal>> pTimeConflict = new HashMap<>();

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Time t : c.getTimes().keySet()) {
                if (!pTime.containsKey(t)) {
                    pTime.put(t, Factory.getModel().newBoolVar("xpt_" + t));
                }
            }
        }

        for (Time p : pTime.keySet()) {
            for (Time q : pTime.keySet()) {
                if (p.equals(q)) {
                    continue;
                }
                this.resolveDistributionConflictWithTime(d.getType(), p, q, pTime.get(p), pTime.get(q), d.isRequired(), d.getPenalty());
            }
        }

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Placement p : c.getPlacements().keySet()) {
                Time t = p.getTime();
                Literal cpl = c.getPlacements().get(p);
                Literal ctd = pTime.get(t);
                Factory.getModel().addImplication(cpl, ctd);
                if (!pTimeConflict.containsKey(t)) {
                    pTimeConflict.put(t, new ArrayList<>());
                }
                pTimeConflict.get(t).add(cpl);
            }
        }
        if (d.isRequired()) {
            for (Time t : pTimeConflict.keySet()) {
                Factory.getModel().addAtMostOne(pTimeConflict.get(t).toArray(new Literal[0]));
            }
        } else {
            for (Time t : pTimeConflict.keySet()) {
                addSoftDistributionConstraint(d, pTimeConflict.get(t));
            }
        }
    }

    private void distributionConflictWithRoom(Distribution d) {
        Map<Room, Literal> pRoom = new HashMap<>();
        Map<Room, ArrayList<Literal>> pRoomConflict = new HashMap<>();

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Room r : c.getRooms().keySet()) {
                if (!pRoom.containsKey(r)) {
                    pRoom.put(r, Factory.getModel().newBoolVar("xpr_" + r));
                }
            }
        }

        for (Room p : pRoom.keySet()) {
            for (Room q : pRoom.keySet()) {
                if (p.equals(q)) {
                    continue;
                }
                this.resolveDistributionConflictWithRoom(d.getType(), p, q, pRoom.get(p), pRoom.get(q), d.isRequired(), d.getPenalty());
            }
        }

        for (int i = 0; i < d.getClassList().size(); i++) {
            Class c = classes.get(d.getClassList().get(i));
            for (Placement p : c.getPlacements().keySet()) {
                Room t = p.getRoom();
                Literal cpl = c.getPlacements().get(p);
                Literal ctr = pRoom.get(t);
                Factory.getModel().addImplication(cpl, ctr);
                if (!pRoomConflict.containsKey(t)) {
                    pRoomConflict.put(t, new ArrayList<>());
                }
            }
        }

        if (d.isRequired()) {
            for (Room t : pRoomConflict.keySet()) {
                Factory.getModel().addAtMostOne(pRoomConflict.get(t).toArray(new Literal[0]));
            }
        } else {
            for (Room t : pRoomConflict.keySet()) {
                addSoftDistributionConstraint(d, pRoomConflict.get(t));
            }
        }
    }

    private void addSoftDistributionConstraint(Distribution d, ArrayList<Literal> literals) {
        for (int i = 0; i < literals.size(); i++) {
            for (int j = i + 1; j < literals.size(); j++) {
                Utils.addDistributionConstraint(literals.get(i), literals.get(j), d.isRequired(), d.getPenalty());
            }
        }
    }

    private void resolveDistributionConflictWithPlacement(String type, Placement p, Placement q, Literal l1, Literal l2, boolean isRequired, int penalty) {
        if (type.equals("SameAttendees")) {
            SameAttendees.resolve(p, q, l1, l2, isRequired, penalty);
        }
    }

    private void resolveDistributionConflictWithTime(String type, Time p, Time q, Literal l1, Literal l2, boolean isRequired, int penalty) {
        switch (type) {
            case "DifferentDays":
                DifferentDays.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "DifferentTime":
                DifferentTime.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "DifferentWeeks":
                DifferentWeeks.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "NotOverlap":
                NotOverlap.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "Overlap":
                Overlap.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "Precedence":
                Precedence.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "SameDays":
                SameDays.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "SameStart":
                SameStart.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "SameTime":
                SameTime.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "SameWeeks":
                SameWeeks.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            default:
                break;
        }
        if (MinGap.isMinGap(type)) {
            MinGap.resolve(p, q, MinGap.getG(type), l1, l2, isRequired, penalty);
        }
        if (WorkDay.isWorkDayType(type)) {
            WorkDay.resolve(p, q, WorkDay.getS(type), l1, l2, isRequired, penalty);
        }
    }

    private void resolveDistributionConflictWithRoom(String type, Room p, Room q, Literal l1, Literal l2, boolean isRequired, int penalty) {
        switch (type) {
            case "DifferentRoom":
                DifferentRoom.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            case "SameRoom":
                SameRoom.resolve(p, q, l1, l2, isRequired, penalty);
                break;
            default:
                break;
        }
    }
}
