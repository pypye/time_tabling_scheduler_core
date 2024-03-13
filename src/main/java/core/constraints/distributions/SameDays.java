package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class SameDays {

    public static boolean compare(Time i, Time j) {
        // ((Ci.days or Cj.days) = Ci.days) ∨ ((Ci.days or Cj.days) = Cj.days)
        String orResult = Utils.orDays(i, j);
        return orResult.equals(i.getDays()) || orResult.equals(j.getDays());
    }
}
