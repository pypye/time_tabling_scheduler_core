package core.constraints.distributions;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

import java.util.List;

public class MaxDays {

    public void add(int day) {
        List<Class> classList = Factory.getProblem().getClassList();
        Literal[] dayLiterals = new Literal[Factory.getProblem().getNrDays()];
        for (int i = 0; i < Factory.getProblem().getNrDays(); i++) {
            Literal[] time_j = new Literal[classList.size()];
            for (int k = 0; k < classList.size(); k++) {
                time_j[k] = classList.get(k).day[i];
            }
            dayLiterals[i] = ConstraintHandler.addConstraint(
                Factory.getModel().addBoolOr(time_j)
            );
        }
        LinearExprBuilder countBits = LinearExpr.newBuilder().addSum(dayLiterals);
        Factory.getModel().addLessOrEqual(countBits.build(), day); // countBits <= day

    }
}
