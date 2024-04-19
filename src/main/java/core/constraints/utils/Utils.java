package core.constraints.utils;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.Time;

public class Utils {
    public static String andDays(Time i, Time j) {
        StringBuilder andResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            int temp_i = i.getDays().charAt(x) - '0';
            int temp_j = j.getDays().charAt(x) - '0';
            andResult.append(temp_i & temp_j);
        }
        return andResult.toString();
    }

    public static String andWeeks(Time i, Time j) {
        StringBuilder andResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            int temp_i = i.getWeeks().charAt(x) - '0';
            int temp_j = j.getWeeks().charAt(x) - '0';
            andResult.append(temp_i & temp_j);
        }
        return andResult.toString();
    }

    public static String orDays(Time i, Time j) {
        StringBuilder orResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            int temp_i = i.getDays().charAt(x) - '0';
            int temp_j = j.getDays().charAt(x) - '0';
            orResult.append(temp_i | temp_j);
        }
        return orResult.toString();
    }

    public static String orWeeks(Time i, Time j) {
        StringBuilder orResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            int temp_i = i.getWeeks().charAt(x) - '0';
            int temp_j = j.getWeeks().charAt(x) - '0';
            orResult.append(temp_i | temp_j);
        }
        return orResult.toString();
    }

    public static void addDistributionConstraint(Literal t1, Literal t2, boolean isRequired, int penalty) {
        if (isRequired) {
            Factory.getModel().addBoolOr(new Literal[]{t1.not(), t2.not()});
        } else {
            Constraint pos = Factory.getModel().addBoolAnd(new Literal[]{t1, t2});
            Constraint neg = Factory.getModel().addBoolOr(new Literal[]{t1.not(), t2.not()});
            Literal l = ConstraintHandler.addConstraint(pos, neg);
            LinearExpr expr = LinearExpr.weightedSum(
                new Literal[]{l},
                new long[]{(long) Factory.getProblem().getOptimization().getDistribution() * penalty}
            );
            Factory.getProblem().getSoftDistributionExpr().add(expr);
        }
    }
}
