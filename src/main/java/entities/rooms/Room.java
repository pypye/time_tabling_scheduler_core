package entities.rooms;

import entities.Time;

import java.util.List;

import utils.StringFormatter;

public class Room {
    private String id;
    private int capacity;
    private List<Travel> travelList;
    private List<Time> unavailableList;

    public Room() {
    }

    public Room(String id, int capacity, List<Travel> travelList, List<Time> unavailableList) {
        this.id = id;
        this.capacity = capacity;
        this.travelList = travelList;
        this.unavailableList = unavailableList;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Travel> getTravelList() {
        return this.travelList;
    }

    public void setTravelList(List<Travel> travelList) {
        this.travelList = travelList;
    }

    public List<Time> getUnavailableList() {
        return this.unavailableList;
    }

    public void setUnavailableList(List<Time> unavailableList) {
        this.unavailableList = unavailableList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
