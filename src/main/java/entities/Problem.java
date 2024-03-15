package entities;

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

    private Map<String, Room> rooms;
    private Map<String, Time> times;
    private Map<String, Course> courses;
    private Map<String, Class> classes;
    private Map<String, Student> students;
    private List<Distribution> distributions;

    private final Map<Placement, ArrayList<Literal>> placementConflicts;

    private final Map<Placement, Literal> placements;

    private final Set<String> resolvedHardDistributions;

    public Problem() {
        rooms = new HashMap<>();
        times = new HashMap<>();
        courses = new HashMap<>();
        classes = new LinkedHashMap<>();
        students = new HashMap<>();
        distributions = new ArrayList<>();
        placements = new HashMap<>();
        placementConflicts = new HashMap<>();
        resolvedHardDistributions = new HashSet<>();
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
            if (d.isRequired()) {
                if(!MaxDays.isMaxDays(d.getType())
                    && !MaxDayLoad.isMaxDayLoad(d.getType())
                    && !MaxBreaks.isMaxBreaks(d.getType())
                    && !MaxBlock.isMaxBlock(d.getType())) {
                    for (int i = 0; i < d.getClassList().size(); i++) {
                        for (int j = i + 1; j < d.getClassList().size(); j++) {
                            Class c1 = classes.get(d.getClassList().get(i));
                            Class c2 = classes.get(d.getClassList().get(j));
                            String key;
                            if (c1.getId().compareTo(c2.getId()) < 0) {
                                key = d.getType() + "_" + c1.getId() + "_" + c2.getId();
                            } else {
                                key = d.getType() + "_" + c2.getId() + "_" + c1.getId();
                            }
                            if (!resolvedHardDistributions.contains(key)) {
                                c1.resolveDistributionConflict(d.getType(), c2);
                                resolvedHardDistributions.add(key);
                            }
                        }
                    }
                } else {
                    if (MaxDays.isMaxDays(d.getType())) {
                        MaxDays.resolve(d.getClassList(), MaxDays.getD(d.getType()));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
