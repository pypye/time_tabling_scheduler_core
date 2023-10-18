package entities.courses;

import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.rooms.Room;
import utils.StringFormatter;

import java.util.List;

public class Class {
    private String id;
    private int limit;
    private List<Room> roomList;
    private List<Integer> roomsPenaltyList;
    private List<Time> availableTimeList;

    private List<Integer> timePenaltyList;

    public IntVar room = null;
    public IntVar hour = null;
    public Literal[] day = null;
    public Literal[] week = null;

    public Class() {
    }

    public Class(String id, int limit, List<Room> roomList, List<Integer> roomsPenaltyList, List<Time> availableTimeList, List<Integer> timePenaltyList) {
        this.id = id;
        this.limit = limit;
        this.roomList = roomList;
        this.roomsPenaltyList = roomsPenaltyList;
        this.availableTimeList = availableTimeList;
        this.timePenaltyList = timePenaltyList;
        this.initSolverConstraint();
    }

    public void initSolverConstraint() {
        this.room = Factory.getModel().newIntVar(1, Factory.getProblem().getNumRooms(), "room_" + id);
        this.hour = Factory.getModel().newIntVar(0, Factory.getProblem().getSlotsPerDay(), "hour_" + id);
        this.day = new Literal[Factory.getProblem().getNrDays()];
        this.week = new Literal[Factory.getProblem().getNrWeeks()];
        for (int j = 0; j < Factory.getProblem().getNrDays(); j++) {
            day[j] = Factory.getModel().newBoolVar("day_" + id + "_slot_" + j);
        }
        for (int j = 0; j < Factory.getProblem().getNrWeeks(); j++) {
            week[j] = Factory.getModel().newBoolVar("week_" + id + "_slot_" + j);
        }
        Factory.getModel().addAtLeastOne(day);
        Factory.getModel().addAtLeastOne(week);
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

    public List<Integer> getRoomsPenaltyList() {
        return roomsPenaltyList;
    }

    public void setRoomsPenaltyList(List<Integer> roomsPenaltyList) {
        this.roomsPenaltyList = roomsPenaltyList;
    }

    public List<Time> getAvailableTimeList() {
        return availableTimeList;
    }

    public void setAvailableTimeList(List<Time> availableTimeList) {
        this.availableTimeList = availableTimeList;
    }

    public List<Integer> getTimePenaltyList() {
        return timePenaltyList;
    }

    public void setTimePenaltyList(List<Integer> timePenaltyList) {
        this.timePenaltyList = timePenaltyList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
