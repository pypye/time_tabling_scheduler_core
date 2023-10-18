package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;


public class DifferentDays {

    public static void add(Class i, Class j) {
        Literal diffDay = ConstraintHandler.addTimeSlotConstraintAnd(i.day, j.day).not(); // (c_i.days and c_j.days) = 0
        Factory.getModel().addBoolOr(new Literal[]{diffDay});
    }
}
