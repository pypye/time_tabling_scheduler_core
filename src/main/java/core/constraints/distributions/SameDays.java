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

    public static void add(Class i, Class j) {
        for (int k = 0; k < i.getAvailableTimeList().size(); k++) {
            Time t1 = i.getAvailableTimeList().get(k);
            for (int l = 0; l < j.getAvailableTimeList().size(); l++) {
                Time t2 = j.getAvailableTimeList().get(l);
                if (!SameDays.compare(t1, t2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.time[k].not(), j.time[l].not()
                    });
                }
            }
        }
    }
}
