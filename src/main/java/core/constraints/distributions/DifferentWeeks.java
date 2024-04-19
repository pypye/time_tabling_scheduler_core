package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

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

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (DifferentWeeks.compare(p.getTime(), q.getTime())) {
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
