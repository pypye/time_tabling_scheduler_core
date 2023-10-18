package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class NotOverlap {
    /**
     * <p>Description: No two classes in this constraint can overlap in time.
     * <p>Formula: (c_i.end ≤ c_j.start) ∨ (c_j.end ≤ c_i.start) ∨ ((c_i.days and c_j.days) = 0) ∨ ((c_i.weeks and c_j.weeks) = 0)
     */
    public static void add(Class i, Class j) {
        Literal diffTime1 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(i.end, j.start)); // (c_i.end ≤ c_j.start)
        Literal diffTime2 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(j.end, i.start)); // (c_j.end ≤ c_i.start)
        Literal diffDay = ConstraintHandler.addTimeSlotConstraintAnd(i.day, j.day).not(); // (c_i.days and c_j.days) = 0
        Literal diffWeek = ConstraintHandler.addTimeSlotConstraintAnd(i.week, j.week).not(); // (c_i.weeks and c_j.weeks) = 0
        Factory.getModel().addBoolOr(new Literal[]{diffTime1, diffTime2, diffDay, diffWeek});
    }
}
