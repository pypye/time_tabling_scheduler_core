package entities.courses;

import com.google.ortools.sat.Literal;
import core.constraints.distributions.NotOverlap;
import core.constraints.distributions.Overlap;
import core.constraints.distributions.SameAttendees;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.rooms.Room;
import utils.StringFormatter;

import java.util.*;

public class Class {
    private String id;
    private int limit;
    private List<Room> roomList;
    private List<Time> availableTimeList;

    public Set<Placement> placements = new HashSet<>();
    public Map<Placement, Literal> placementLiterals = new HashMap<>();

    public Map<Class, Set<String>> distributions = new HashMap<>();


    public Class() {
    }

    public void init() {
        if (!this.roomList.isEmpty()) {
            for (Room r : this.roomList) {
                for (Time t : this.availableTimeList) {
                    if (!checkUnavailableTime(r, t)) {
                        this.placements.add(new Placement(r, t));
                        Factory.getPlacements().add(new Placement(r, t));
                    }
                }
            }
        } else {
            for (Time t : this.availableTimeList) {
                this.placements.add(new Placement(null, t));
                Factory.getPlacements().add(new Placement(null, t));
            }
        }
    }

    public static boolean checkUnavailableTime(Room r, Time t) {
        for (Time u : r.getUnavailableList()) {
            if (Overlap.compare(u, t)) {
                return true;
            }
        }
        return false;
    }

    public void removeDistributionConstraint(String type, Class x) {
        if (!distributions.containsKey(x)) {
            distributions.put(x, new HashSet<>());
        }
        distributions.get(x).add(type);
        switch (type) {
            case "SameAttendees":
                SameAttendees.remove(this, x);
                break;
            case "NotOverlap":
                NotOverlap.remove(this, x);
                break;
        }
    }

    public void makeSolverConstraints() {
        for (Placement p : this.placements) {
            Literal literal = Factory.getModel().newBoolVar("class_" + this.id + "_placement_" + p);
            this.placementLiterals.put(p, literal);
            Literal pl = Factory.getPlacementLiterals().get(p);
            Factory.getModel().addImplication(literal, pl);
            if (!Factory.getPlacementConflicts().containsKey(p)) {
                Factory.getPlacementConflicts().put(p, new ArrayList<>());
            }
            Factory.getPlacementConflicts().get(p).add(literal);
        }
        Factory.getModel().addExactlyOne(this.placementLiterals.values());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Time> getAvailableTimeList() {
        return availableTimeList;
    }

    public void setAvailableTimeList(List<Time> availableTimeList) {
        this.availableTimeList = availableTimeList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
