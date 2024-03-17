package entities;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import core.constraints.defaults.TwoClassOverlap;
import core.constraints.distributions.MaxBlock;
import core.constraints.distributions.MaxBreaks;
import core.constraints.distributions.MaxDayLoad;
import core.constraints.distributions.MaxDays;
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
        Set<String> resolvedDistributions = new HashSet<>();
        int count = 0;
        for (Distribution d : distributions) {
            count++;
            System.out.println("Distribution " + count + " of " + distributions.size() + " : " + d);
            if (!MaxDays.isMaxDays(d.getType())
                && !MaxDayLoad.isMaxDayLoad(d.getType())
                && !MaxBreaks.isMaxBreaks(d.getType())
                && !MaxBlock.isMaxBlock(d.getType())) {
                for (int i = 0; i < d.getClassList().size(); i++) {
                    for (int j = i + 1; j < d.getClassList().size(); j++) {
                        Class c1 = classes.get(d.getClassList().get(i));
                        Class c2 = classes.get(d.getClassList().get(j));
                        String key;
                        if (c1.getId().compareTo(c2.getId()) < 0) {
                            key = d.getType() + "_" + c1.getId() + "_" + c2.getId() + "_" + d.isRequired() + "_" + d.getPenalty();
                        } else {
                            key = d.getType() + "_" + c2.getId() + "_" + c1.getId() + "_" + d.isRequired() + "_" + d.getPenalty();
                        }
                        if (!resolvedDistributions.contains(key)) {
                            c1.resolveDistributionConflict(d.getType(), c2, d.isRequired(), d.getPenalty());
                            resolvedDistributions.add(key);
                        }
                    }
                }
            } else {
                if (d.isRequired()) {
                    if (MaxDays.isMaxDays(d.getType())) {
                        MaxDays.resolve(d.getClassList(), MaxDays.getD(d.getType()));
                    }
                    if (MaxDayLoad.isMaxDayLoad(d.getType())) {
                        MaxDayLoad.resolve(d.getClassList(), MaxDayLoad.getS(d.getType()));
                    }
                }
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
}
