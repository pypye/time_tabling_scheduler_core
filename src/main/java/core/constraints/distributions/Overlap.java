package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class Overlap {
    /**
     * <p>Description: Any two classes in this constraint must have some overlap in time.
     * <p>Formula: (c_j.start < c_i.end) ∧ (c_i.start < c_j.end) ∧ ((c_i.days and c_j.days) ≠ 0) ∧ ((c_i.weeks and c_j.weeks) ≠ 0)
     * @param i class
     * @param j class
     */
    public static void add(Class i, Class j) {
        Literal overlapTime1 = ConstraintHandler.addConstraint(Factory.getModel().addLessThan(j.start, i.end)); // (c_j.start < c_i.end)
        Literal overlapTime2 = ConstraintHandler.addConstraint(Factory.getModel().addLessThan(i.start, j.end)); // (c_i.start < c_j.end)
        Literal overlapDay = ConstraintHandler.addTimeSlotConstraintAnd(i.day, j.day); // (c_i.days and c_j.days) ≠ 0)
        Literal overlapWeek = ConstraintHandler.addTimeSlotConstraintAnd(i.week, j.week); // (c_i.weeks and c_j.weeks) ≠ 0)
        Factory.getModel().addBoolAnd(new Literal[]{overlapTime1, overlapTime2, overlapDay, overlapWeek});
    }
}
