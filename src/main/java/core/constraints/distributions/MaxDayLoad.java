package core.constraints.distributions;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;

public class MaxDayLoad {
    public static boolean isMaxDayLoad(String t) {
        return t.contains("MaxDayLoad");
    }

    public static int getS(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }

    // n classes
    // L[i] = co[i]_c1 * t[i]_c1 + co[i]_c2 * t[i]_c2 + ... + co[i]_cn * t[i]_cn
    // F = L[1] + L[2] + ... + L[n] <= S
    public static void resolve(ArrayList<String> classes, int S) {
        for (int i = 0; i < Factory.getProblem().getNrDays(); i++) {
            for (int j = 0; j < Factory.getProblem().getNrWeeks(); j++) {
                ArrayList<Literal> lit = new ArrayList<>();
                ArrayList<Long> co = new ArrayList<>();
                for (String c : classes) {
                    Class cl = Factory.getProblem().getClasses().get(c);
                    for (Time t : cl.getTimes().keySet()) {
                        if (t.getDays().charAt(i) == '1' && t.getWeeks().charAt(j) == '1') {
                            lit.add(cl.getTimes().get(t));
                            co.add((long) t.getLength());
                        }
                    }
                }
                if (!lit.isEmpty()) {
                    LinearExpr expr = LinearExpr.weightedSum(lit.toArray(new Literal[0]), co.stream().mapToLong(l -> l).toArray());
                    Factory.getModel().addLessOrEqual(expr, S);
                }
            }
        }
    }
}
