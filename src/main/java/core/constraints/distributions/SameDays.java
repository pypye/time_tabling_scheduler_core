package core.constraints.distributions;

import core.constraints.utils.Utils;
import entities.Time;

public class SameDays {

    public static boolean compare(Time i, Time j) {
        // ((Ci.days or Cj.days) = Ci.days) ∨ ((Ci.days or Cj.days) = Cj.days)
        String orResult = Utils.orDays(i, j);
        return orResult.equals(i.getDays()) || orResult.equals(j.getDays());
    }
}
