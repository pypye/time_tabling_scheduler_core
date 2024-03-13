package entities.courses;

import utils.StringFormatter;

import java.util.Map;

public class Course {
    private String id;
    private Map<String, Config> configList;

    public Course() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Config> getConfigList() {
        return configList;
    }

    public void setConfigList(Map<String, Config> configList) {
        this.configList = configList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
