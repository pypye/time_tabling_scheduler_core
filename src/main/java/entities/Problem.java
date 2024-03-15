package entities;

import com.google.ortools.sat.Literal;
import core.constraints.defaults.TwoClassOverlap;
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

    public Problem() {
        rooms = new HashMap<>();
        times = new HashMap<>();
        courses = new HashMap<>();
        classes = new LinkedHashMap<>();
        students = new HashMap<>();
        distributions = new ArrayList<>();
        placements = new HashMap<>();
        placementConflicts = new HashMap<>();
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

    public void makePlacementLiterals() {
        for (Placement p : placements.keySet()) {
            Literal l;
            if (p.getRoom() == null) {
                l = Factory.getModel().newBoolVar("placement_" + p.getTime());
            } else {
                l = Factory.getModel().newBoolVar("placement_" + p.getRoom().getId() + "_" + p.getTime());
            }
            placements.put(p, l);
        }
        ArrayList<Placement> placementList = new ArrayList<>(placements.keySet());
        for (int i = 0; i < placementList.size(); i++) {
            Placement p = placementList.get(i);
            for (int j = i + 1; j < placementList.size(); j++) {
                Placement q = placementList.get(j);
                if (p.equals(q)) {
                    continue;
                }
                if (p.getRoom() == null || q.getRoom() == null) {
                    continue;
                }
                if (TwoClassOverlap.compare(p.getRoom(), q.getRoom(), p.getTime(), q.getTime())) {
                    Factory.getModel().addBoolOr(new Literal[]{placements.get(p).not(), placements.get(q).not()});
                }
            }
        }
    }

    public void resolvePlacementLiteralsConflict() {
        for (Placement p : placements.keySet()) {
            if (placementConflicts.containsKey(p)) {
                ArrayList<Literal> conflicts = placementConflicts.get(p);
                Factory.getModel().addAtMostOne(conflicts.toArray(new Literal[0]));
            }
        }
    }

    public void prepareDistribution() {
        for (Distribution d : distributions) {
            System.out.println(d);
        }
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
