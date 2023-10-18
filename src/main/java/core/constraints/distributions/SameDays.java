package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class SameDays {

    public static void add(Class i, Class j) {
        Literal sameDay1 = ConstraintHandler.addTimeSlotConstrainOr(i.day, j.day, i.day);
        Literal sameDay2 = ConstraintHandler.addTimeSlotConstrainOr(i.day, j.day, j.day);
        Factory.getModel().addBoolOr(new Literal[]{sameDay1, sameDay2});
    }
}
