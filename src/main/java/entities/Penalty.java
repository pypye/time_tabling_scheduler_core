package entities;

public class Penalty<T> {
    private T entity;
    private int penalty;

    public Penalty() {
    }

    public Penalty(T entity, int penalty) {
        this.entity = entity;
        this.penalty = penalty;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}
