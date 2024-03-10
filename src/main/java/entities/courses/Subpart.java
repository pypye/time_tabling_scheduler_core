package entities.courses;

import utils.StringFormatter;

import java.util.List;

public class Subpart {
    private List<Class> classList;

    public Subpart() {
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
