package entities;

import utils.StringFormatter;

import java.util.List;

public class Distribution {
    private String type;

    private List<String> classList;

    private boolean required;

    private int penalty;

    public Distribution() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Distribution that = (Distribution) obj;
        return required == that.required && penalty == that.penalty && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + penalty;
        return result;
    }
}
