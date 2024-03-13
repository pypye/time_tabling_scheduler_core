package entities.courses;

import com.google.ortools.sat.Literal;
import core.constraints.defaults.Unavailable;
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
import java.util.Set;

public class Class implements Comparable<Class> {
    private String id;
    private int limit;
    private ArrayList<Penalty<Room>> roomList;
    private ArrayList<Penalty<Time>> timeList;

    private final Map<Placement, Literal> placements = new HashMap<>();

    private final Map<Class, Set<String>> distributions = new HashMap<>();


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
                        this.placements.put(p, null);
                        Factory.getProblem().getPlacements().put(p, null);
                    }
                }
            }
        } else {
            for (Penalty<Time> pt : this.timeList) {
                Time t = pt.getEntity();
                Placement p = new Placement(null, t);
                this.placements.put(p, null);
                Factory.getProblem().getPlacements().put(p, null);
            }
        }
    }

    public void mapClassPlacementToGlobalPlacement() {
        for (Placement p : this.placements.keySet()) {
            Literal cpl;
            if (p.getRoom() != null) {
                cpl = Factory.getModel().newBoolVar("class_" + this.id + "_placement_" + p.getRoom().getId() + "_" + p.getTime());
            } else {
                cpl = Factory.getModel().newBoolVar("class_" + this.id + "_placement_" + p.getTime());
            }
            Literal pl = Factory.getProblem().getPlacements().get(p);
            this.placements.put(p, cpl);
            Factory.getModel().addImplication(cpl, pl);

            if (!Factory.getProblem().getPlacementConflicts().containsKey(p)) {
                Factory.getProblem().getPlacementConflicts().put(p, new ArrayList<>());
            }
            Factory.getProblem().getPlacementConflicts().get(p).add(cpl);

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

    public Map<Class, Set<String>> getDistributions() {
        return distributions;
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
}
