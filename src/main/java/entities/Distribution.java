package entities;

import entities.courses.Class;

import java.util.List;

public class Distribution {
    private String type;

    private List<Class> classList;

    public Distribution() {
    }

    public Distribution(String type, List<Class> classList) {
        this.type = type;
        this.classList = classList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }
}
