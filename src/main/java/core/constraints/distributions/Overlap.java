package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class Overlap {
    public static boolean compare(Time i, Time j) {
        // (Cj.start < Ci.end) ∧ (Ci.start < Cj.end) ∧ ((Ci.days and Cj.days) ≠ 0) ∧ ((Ci.weeks and Cj.weeks) ≠ 0)
        return j.getStart() < i.getEnd() && i.getStart() < j.getEnd() && getOverlapDays(i, j) && getOverlapWeeks(i, j);
    }

    public static boolean getOverlapWeeks(Time i, Time j) {
        // (Ci.weeks and Cj.weeks) ≠ 0
        String andResult = Utils.andWeeks(i, j);
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            if (andResult.charAt(x) == '1') {
                return true;
            }
        }
        return false;
    }

    public static boolean getOverlapDays(Time i, Time j) {
        // (Ci.days and Cj.days) ≠ 0
        String andResult = Utils.andDays(i, j);
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            if (andResult.charAt(x) == '1') {
                return true;
            }
        }
        return false;
    }
}
