package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class DifferentTime {
    public static boolean compare(Time i, Time j) {
        // (Ci.end ≤ Cj.start) ∨ (Cj.end ≤ Ci.start)
        return i.getEnd() <= j.getStart() || j.getEnd() <= i.getStart();
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (DifferentTime.compare(p.getTime(), q.getTime())) {
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

    public static void resolve(Class i, Class j, boolean isRequired, int penalty) {
        for (Time t1 : Factory.getProblem().getTimes().values()) {
            if (i.getTimes().get(t1) == null) {
                continue;
            }
            for (Time t2 : Factory.getProblem().getTimes().values()) {
                if (j.getTimes().get(t2) == null) {
                    continue;
                }
                if (!DifferentTime.compare(t1, t2)) {
                    Utils.addDistributionConstraint(i.getTimes().get(t1), j.getTimes().get(t2), isRequired, penalty);
                }
            }
        }
    }
}
