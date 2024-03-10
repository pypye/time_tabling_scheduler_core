package entities;

import entities.courses.Class;

import java.util.List;

public class Distribution {
    private String type;

    private List<Class> classList;

    private boolean required;

    public Distribution() {
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
