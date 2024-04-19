package entities.courses;

import com.google.ortools.sat.Literal;
import core.constraints.defaults.Unavailable;
import core.constraints.distributions.*;
import core.solver.Factory;
import entities.Penalty;
import entities.Placement;
import entities.Time;
import entities.rooms.Room;
import org.jetbrains.annotations.NotNull;
import utils.StringFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Class implements Comparable<Class> {
    private String id;
    private int limit;

    private String parentId = null;
    private ArrayList<Penalty<Room>> roomList;
    private ArrayList<Penalty<Time>> timeList;

    private final Map<Placement, Literal> placements = new HashMap<>();

    private final Map<Room, Literal> rooms = new HashMap<>();

    private final Map<Time, Literal> times = new HashMap<>();

    public Class() {
    }

    public void init() {
        if (!this.roomList.isEmpty()) {
            for (Penalty<Room> pr : this.roomList) {
                Room r = pr.getEntity();
                for (Penalty<Time> pt : this.timeList) {
                    Time t = pt.getEntity();
                    if (!Unavailable.compare(r, t)) {
                        Placement p = new Placement(r, t);
                        this.addPlacement(p);
                    }
                }
            }
        } else {
            for (Penalty<Time> pt : this.timeList) {
                Time t = pt.getEntity();
                Placement p = new Placement(null, t);
                this.addPlacement(p);
            }
        }
    }

    private void addPlacement(Placement p) {
        this.placements.put(p, null);
        Factory.getProblem().getPlacements().put(p, null);
        if (!Factory.getProblem().getPlacementClasses().containsKey(p)) {
            Factory.getProblem().getPlacementClasses().put(p, new ArrayList<>());
        }
        Factory.getProblem().getPlacementClasses().get(p).add(this);
    }

    public void makeRoomAndTimeLiteral() {
        if (!this.roomList.isEmpty()) {
            for (Penalty<Room> pr : this.roomList) {
                Room r = pr.getEntity();
                Literal lr = Factory.getModel().newBoolVar("c" + this.id + "_r" + r);
                this.rooms.put(r, lr);
            }
            Factory.getModel().addExactlyOne(this.rooms.values());
        }
        for (Penalty<Time> pt : this.timeList) {
            Time t = pt.getEntity();
            Literal lt = Factory.getModel().newBoolVar("c" + this.id + "_t" + t);
            this.times.put(t, lt);
        }
        Factory.getModel().addExactlyOne(this.times.values());
    }

    public void mapClassPlacementToGlobalPlacement() {
        for (Placement p : this.placements.keySet()) {
            Literal cpl = Factory.getModel().newBoolVar("c" + this.id + "_" + p);
            Literal cr = this.rooms.get(p.getRoom());
            Literal ct = this.times.get(p.getTime());
            Literal pl = Factory.getProblem().getPlacements().get(p);
            this.placements.put(p, cpl);
            Factory.getModel().addImplication(cpl, pl);
            if (cr != null) {
                Factory.getModel().addImplication(cpl, cr);
            }
            Factory.getModel().addImplication(cpl, ct);

            if (!this.getRoomList().isEmpty()) {
                if (!Factory.getProblem().getPlacementConflicts().containsKey(p)) {
                    Factory.getProblem().getPlacementConflicts().put(p, new ArrayList<>());
                }
                Factory.getProblem().getPlacementConflicts().get(p).add(cpl);
            }
        }
        Factory.getModel().addExactlyOne(this.placements.values());
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ArrayList<Penalty<Room>> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<Penalty<Room>> roomList) {
        this.roomList = roomList;
    }

    public ArrayList<Penalty<Time>> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<Penalty<Time>> timeList) {
        this.timeList = timeList;
    }

    public Map<Placement, Literal> getPlacements() {
        return placements;
    }

    public Map<Room, Literal> getRooms() {
        return rooms;
    }

    public Map<Time, Literal> getTimes() {
        return times;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }

    @Override
    public int compareTo(@NotNull Class o) {
        int id1 = Integer.parseInt(this.id);
        int id2 = Integer.parseInt(o.id);
        return Integer.compare(id1, id2);
    }

    public void removeDistributionConflict(String type, Class x) {
        switch (type) {
            case "DifferentDays":
                DifferentDays.remove(this, x);
                break;
            case "DifferentRoom":
                DifferentRoom.remove(this, x);
                break;
            case "DifferentTime":
                DifferentTime.remove(this, x);
                break;
            case "DifferentWeeks":
                DifferentWeeks.remove(this, x);
                break;
            case "NotOverlap":
                NotOverlap.remove(this, x);
                break;
            case "Overlap":
                Overlap.remove(this, x);
                break;
            case "SameAttendees":
                SameAttendees.remove(this, x);
                break;
            case "SameDays":
                SameDays.remove(this, x);
                break;
            case "SameRoom":
                SameRoom.remove(this, x);
                break;
            case "SameStart":
                SameStart.remove(this, x);
                break;
            case "SameTime":
                SameTime.remove(this, x);
                break;
            case "SameWeeks":
                SameWeeks.remove(this, x);
            default:
                break;
        }
        if (MinGap.isMinGap(type)) {
            MinGap.remove(this, x, MinGap.getG(type));
        }
        if (WorkDay.isWorkDayType(type)) {
            WorkDay.remove(this, x, WorkDay.getS(type));
        }
    }
}
