package core.constraints.distributions;

import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Time;

public class DifferentWeeks {
    public static boolean compare(Time i, Time j) {
        // (Ci.weeks and Cj.weeks) = 0
        String andResult = Utils.andWeeks(i, j);
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            if (andResult.charAt(x) == '1') {
                return false;
            }
        }
        return true;
    }
}
