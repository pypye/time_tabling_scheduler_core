package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;
public class SameWeeks {

    public static void add(Class i, Class j) {
        Literal sameWeek1 = ConstraintHandler.addTimeSlotConstrainOr(i.week, j.week, i.week);
        Literal sameWeek2 = ConstraintHandler.addTimeSlotConstrainOr(i.week, j.week, j.week);
        Factory.getModel().addBoolOr(new Literal[]{sameWeek1, sameWeek2});
    }
}
