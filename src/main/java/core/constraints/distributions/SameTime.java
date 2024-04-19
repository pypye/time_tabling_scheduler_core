package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class SameTime {
    public static boolean compare(Time i, Time j) {
        // (Ci.start ≤ Cj.start ∧ Cj.end ≤ Ci.end) ∨ (Cj.start ≤ Ci.start ∧ Ci.end ≤ Cj.end)
        if (i.getStart() <= j.getStart() && (j.getEnd() <= i.getEnd())) {
            return true;
        } else {
            return j.getStart() <= i.getStart() && (i.getEnd() <= j.getEnd());
        }
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (SameTime.compare(p.getTime(), q.getTime())) {
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
