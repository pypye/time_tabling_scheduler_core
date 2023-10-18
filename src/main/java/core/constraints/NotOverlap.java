package core.constraints;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class NotOverlap {
    /**
     * Description: No two classes in this constraint can overlap in time.
     * Formula: (c_i.end ≤ c_j.start) ∨ (c_j.end ≤ c_i.start) ∨ ((c_i.days and c_j.days) = 0) ∨ ((c_i.weeks and c_j.weeks) = 0)
     */
    public static void add(Class i, Class j) {
        Literal diffRoom = ConstraintHandler.addConstraint(Factory.getModel().addDifferent(i.room, j.room));
        Literal diffHour = ConstraintHandler.addConstraint(Factory.getModel().addDifferent(i.hour, j.hour));
        Literal sameDay = ConstraintHandler.addTimeSlotConstraint(i.day, j.day);
        Literal sameWeek = ConstraintHandler.addTimeSlotConstraint(i.week, j.week);
        Factory.getModel().addBoolOr(new Literal[]{diffRoom, diffHour, sameDay.not(), sameWeek.not()});
    }
}
