package entities;

import utils.StringFormatter;

import java.util.ArrayList;

public class Student {
    private String id;
    private ArrayList<String> courseList;

    public Student() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
