package core.constraints.distributions;

import entities.courses.Class;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;

public class SameTime {
    public static void add(Class i, Class j) {
        Literal overlap1 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(i.start, j.start));
        Literal overlap2 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(j.end, i.end));
        Literal condition1 = ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(new Literal[]{overlap1, overlap2}));

        Literal overlap3 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(j.start, i.start));
        Literal overlap4 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(i.end, j.end));
        Literal condition2 = ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(new Literal[]{overlap3, overlap4}));

        Factory.getModel().addBoolOr(new Literal[]{condition1, condition2});
    }
}
