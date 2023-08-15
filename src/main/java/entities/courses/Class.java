package entities.courses;

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

    public Class() {
    }

    public Class(String id, int limit, List<Room> roomList, List<Integer> roomsPenaltyList, List<Time> availableTimeList, List<Integer> timePenaltyList) {
        this.id = id;
        this.limit = limit;
        this.roomList = roomList;
        this.roomsPenaltyList = roomsPenaltyList;
        this.availableTimeList = availableTimeList;
        this.timePenaltyList = timePenaltyList;
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
