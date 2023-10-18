package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class DifferentTime {
    public static void add(Class i, Class j) {
        Literal diffTime1 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(i.end, j.start)); // (c_i.end ≤ c_j.start)
        Literal diffTime2 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(j.end, i.start)); // (c_i.end ≤ c_j.start)
        Factory.getModel().addBoolOr(new Literal[]{diffTime1, diffTime2});
    }
}
