package entities.courses;

import utils.StringFormatter;

import java.util.List;

public class Config {
    private List<Subpart> subpartList;

    public Config() {
    }

    public Config(List<Subpart> subpartList) {
        this.subpartList = subpartList;
    }

    public List<Subpart> getSubpartList() {
        return subpartList;
    }

    public void setSubpartList(List<Subpart> subpartList) {
        this.subpartList = subpartList;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
