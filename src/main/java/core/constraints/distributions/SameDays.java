package core.constraints.distributions;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class SameDays {

    public static boolean compare(Time i, Time j) {
        // ((Ci.days or Cj.days) = Ci.days) ∨ ((Ci.days or Cj.days) = Cj.days)
        String orResult = Utils.orDays(i, j);
        return orResult.equals(i.getDays()) || orResult.equals(j.getDays());
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (SameDays.compare(p.getTime(), q.getTime())) {
                    keep = true;
                    break;
                }
            }
            if (!keep) {
                removeList.add(p);
            }
        }
        for (Placement p : removeList) {
            i.getPlacements().remove(p);
        }
    }

    public static void resolve(Time p, Time q, Literal l1, Literal l2, boolean isRequired, int penalty) {
        if (!compare(p, q)) {
            Utils.addDistributionConstraint(l1, l2, isRequired, penalty);
        }
    }
}
