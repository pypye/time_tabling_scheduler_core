package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class Overlap {
    public static boolean compare(Time i, Time j) {
        // (Cj.start < Ci.end) ∧ (Ci.start < Cj.end) ∧ ((Ci.days and Cj.days) ≠ 0) ∧ ((Ci.weeks and Cj.weeks) ≠ 0)
        return j.getStart() < i.getEnd() && i.getStart() < j.getEnd() && getOverlapDays(i, j) && getOverlapWeeks(i, j);
    }

    public static boolean getOverlapWeeks(Time i, Time j) {
        // (Ci.weeks and Cj.weeks) ≠ 0
        String andResult = Utils.andWeeks(i, j);
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            if (andResult.charAt(x) == '1') {
                return true;
            }
        }
        return false;
    }

    public static boolean getOverlapDays(Time i, Time j) {
        // (Ci.days and Cj.days) ≠ 0
        String andResult = Utils.andDays(i, j);
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            if (andResult.charAt(x) == '1') {
                return true;
            }
        }
        return false;
    }
    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (Overlap.compare(p.getTime(), q.getTime())) {
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
                if (!Overlap.compare(t1, t2)) {
                    Utils.addDistributionConstraint(i.getTimes().get(t1), j.getTimes().get(t2), isRequired, penalty);
                }
            }
        }
    }
}
