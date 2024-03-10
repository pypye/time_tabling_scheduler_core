package entities.courses;

import utils.StringFormatter;

import java.util.List;

public class Course {
    private List<Config> configList;

    public Course() {
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
