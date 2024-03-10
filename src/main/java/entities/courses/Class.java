package entities.courses;

import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
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
    private List<Time> availableTimeList;

    public Literal[] room = null;
    public Literal[] time = null;
    public IntVar[] penaltyRoom = null;
    public IntVar[] penaltyTime = null;

    public Class() {
    }

    public void initSolverConstraint() {
        if (!this.roomList.isEmpty()) {
            this.room = new Literal[this.roomList.size()];
            this.penaltyRoom = new IntVar[this.roomList.size()];
            for (int i = 0; i < this.roomList.size(); i++) {
                this.room[i] = Factory.getModel().newBoolVar("class_" + id + "_room_" + i);
                this.penaltyRoom[i] = Factory.getModel().newIntVar(0, 100, "class_" + id + "_penalty_room_" + i);
            }
        }
        this.time = new Literal[this.availableTimeList.size()];
        this.penaltyTime = new IntVar[this.availableTimeList.size()];
        for (int i = 0; i < this.availableTimeList.size(); i++) {
            this.time[i] = Factory.getModel().newBoolVar("class_" + id + "_time_" + i);
            this.penaltyTime[i] = Factory.getModel().newIntVar(0, 100, "class_" + id + "_penalty_time_" + i);
        }
        // class can meet in only specific several room and time and add penalty if not
        if (!this.roomList.isEmpty()) {
            Factory.getModel().addAtLeastOne(this.room);
            for(int i = 0; i < this.roomList.size(); i++) {
                Factory.getModel().addEquality(penaltyRoom[i], this.roomList.get(i).getPenalty()).onlyEnforceIf(room[i]);
                Factory.getModel().addEquality(penaltyRoom[i], 0).onlyEnforceIf(room[i].not());
            }
            LinearExpr penaltyRoomExpr = LinearExpr.sum(this.penaltyRoom);
            Factory.getModel().minimize(penaltyRoomExpr);
        }
        if (!this.availableTimeList.isEmpty()) {
            Factory.getModel().addAtLeastOne(this.time);
            for(int i = 0; i < this.availableTimeList.size(); i++) {
                Factory.getModel().addEquality(penaltyTime[i], this.availableTimeList.get(i).getPenalty()).onlyEnforceIf(time[i]);
                Factory.getModel().addEquality(penaltyTime[i], 0).onlyEnforceIf(time[i].not());
            }
            LinearExpr penaltyTimeExpr = LinearExpr.sum(this.penaltyTime);
            Factory.getModel().minimize(penaltyTimeExpr);
        }
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
