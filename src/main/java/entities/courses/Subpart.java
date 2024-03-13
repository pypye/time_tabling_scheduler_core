package entities.courses;

import utils.StringFormatter;

import java.util.Map;

public class Subpart {
    private String id;
    private Map<String, Class> classList;

    public Subpart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Class> getClassList() {
        return classList;
    }

    public void setClassList(Map<String, Class> classList) {
        this.classList = classList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
