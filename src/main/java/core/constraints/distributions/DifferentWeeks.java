package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class DifferentWeeks {
    public static void add(Class i, Class j) {
        Literal diffWeek = ConstraintHandler.addTimeSlotConstraintAnd(i.week, j.week).not(); // (c_i.weeks and c_j.weeks) = 0
        Factory.getModel().addBoolOr(new Literal[]{diffWeek});
    }
}
