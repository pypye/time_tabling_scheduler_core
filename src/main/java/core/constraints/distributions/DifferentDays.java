package core.constraints.distributions;

import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Time;


public class DifferentDays {
    public static boolean compare(Time i, Time j) {
        // (Ci.days and Cj.days) = 0
        String andResult = Utils.andDays(i, j);
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            if (andResult.charAt(x) == '1') {
                return false;
            }
        }
        return true;
    }
}
