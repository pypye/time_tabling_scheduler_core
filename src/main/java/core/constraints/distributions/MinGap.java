package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class MinGap {
    public static boolean compare(Time i, Time j, int G) {
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0) ∨ (Ci.end + G ≤ Cj.start) ∨ (Cj.end + G ≤ Ci.start)
        return DifferentDays.compare(i, j)
            || DifferentWeeks.compare(i, j)
            || i.getEnd() + G <= j.getStart()
            || j.getEnd() + G <= i.getStart();
    }

    public static boolean isMinGap(String t) {
        return t.contains("MinGap");
    }

    public static int getG(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }

    public static void remove(Class i, Class j, int G) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (MinGap.compare(p.getTime(), q.getTime(), G)) {
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

    public static void resolve(Time p, Time q, int G, Literal l1, Literal l2, boolean isRequired, int penalty) {
        if (!compare(p, q, G)) {
            Utils.addDistributionConstraint(l1, l2, isRequired, penalty);
        }
    }
}
