package entities;

import entities.courses.Class;
import entities.courses.Course;
import entities.rooms.Room;
import utils.StringFormatter;

import java.util.ArrayList;
import java.util.List;

public class Problem {
    private String name;
    private int nrDays;
    private int slotsPerDay;
    private int nrWeeks;

    private List<Room> roomList;
    private List<Course> courseList;
    private List<Student> studentList;

    private List<Distribution> distributionList;

    public Problem() {
    }

    public Problem(String name, int nrDays, int slotsPerDay, int nrWeeks, List<Room> roomList, List<Course> courseList, List<Student> studentList, List<Distribution> distributionList) {
        this.name = name;
        this.nrDays = nrDays;
        this.slotsPerDay = slotsPerDay;
        this.nrWeeks = nrWeeks;
        this.roomList = roomList;
        this.courseList = courseList;
        this.studentList = studentList;
        this.distributionList = distributionList;
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

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public int getNumRooms() {
        return roomList.size();
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }


    public List<Class> getClassList() {
        ArrayList<Class> classList = new ArrayList<>();
        this.courseList
                .stream()
                .flatMap(course -> course.getConfigList().stream())
                .flatMap(config -> config.getSubpartList().stream())
                .flatMap(subpart -> subpart.getClassList().stream())
                .forEach(classList::add);
        return classList;
    }

    public List<Distribution> getDistributionList() {
        return distributionList;
    }

    public void setDistributionList(List<Distribution> distributionList) {
        this.distributionList = distributionList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
