package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class SameWeeks {

    public static boolean compare(Time i, Time j) {
        // (Ci.weeks or Cj.weeks) = Ci.weeks) ∨ (Ci.weeks or Cj.weeks) = Cj.weeks)
        String orResult = Utils.orWeeks(i, j);
        return orResult.equals(i.getWeek()) || orResult.equals(j.getWeek());
    }
}
