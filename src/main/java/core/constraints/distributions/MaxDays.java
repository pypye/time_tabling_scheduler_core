package core.constraints.distributions;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;

import java.util.ArrayList;

public class MaxDays {

    public static boolean isMaxDays(String t) {
        return t.contains("MaxDays");
    }

    public static int getD(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }

    public static void resolve(ArrayList<String> classes, int D) {
        ArrayList<Literal> daysLit = new ArrayList<>();
        for (int i = 0; i < Factory.getProblem().getNrDays(); i++) {
            ArrayList<Literal> lit = new ArrayList<>();
            ArrayList<Literal> litNot = new ArrayList<>();
            for (String c : classes) {
                Class cl = Factory.getProblem().getClasses().get(c);
                lit.add(cl.getDays().get(i));
                litNot.add(cl.getDays().get(i).not());
            }
            Literal l = ConstraintHandler.addConstraint(
                Factory.getModel().addBoolOr(lit),
                Factory.getModel().addBoolAnd(litNot)
            );
            daysLit.add(l);
        }
        LinearExpr expr = LinearExpr.sum(daysLit.toArray(new Literal[0]));
        Factory.getModel().addLessOrEqual(expr, D);
    }
}
