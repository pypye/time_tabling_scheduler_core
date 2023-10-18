package core.constraints.distributions;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

public class MinGap {
    public static void add(Class i, Class j, int gap) {
        Literal diffDay = ConstraintHandler.addTimeSlotConstraintAnd(i.day, j.day).not(); // (c_i.days and c_j.days) = 0
        Literal diffWeek = ConstraintHandler.addTimeSlotConstraintAnd(i.week, j.week).not(); // (c_i.weeks and c_j.weeks) = 0
        LinearExprBuilder endAndGap1 = LinearExpr.newBuilder().add(i.end).add(gap);
        Literal minGap1 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(endAndGap1.build(), j.start)); // (c_i.end + gap ≤ c_j.start)
        LinearExprBuilder endAndGap2 = LinearExpr.newBuilder().add(j.end).add(gap);
        Literal minGap2 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(endAndGap2.build(), i.start)); // (c_j.end + gap ≤ c_i.start)
        Factory.getModel().addBoolOr(new Literal[]{diffDay, diffWeek, minGap1, minGap2});
    }
}
