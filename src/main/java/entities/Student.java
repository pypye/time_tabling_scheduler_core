package entities;

import entities.courses.Course;
import utils.StringFormatter;

import java.util.List;

public class Student {
    private String id;
    private List<Course> courseList;

    public Student() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
