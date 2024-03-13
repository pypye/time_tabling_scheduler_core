package entities.courses;

import utils.StringFormatter;

import java.util.Map;

public class Config {

    private String id;
    private Map<String, Subpart> subpartList;

    public Config() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Subpart> getSubpartList() {
        return subpartList;
    }

    public void setSubpartList(Map<String, Subpart> subpartList) {
        this.subpartList = subpartList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
