package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class WorkDay {
    public static boolean compare(Time i, Time j, int S) {
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0) ∨ (max(Ci.end,Cj.end)−min(Ci.start,Cj.start) ≤ S)
        return DifferentDays.compare(i, j)
            || DifferentWeeks.compare(i, j)
            || Math.max(i.getEnd(), j.getEnd()) - Math.min(i.getStart(), j.getStart()) <= S;
    }

    public static boolean isWorkDayType(String t) {
        return t.contains("WorkDay");
    }

    public static int getS(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }

    public static void remove(Class i, Class j, int S) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (WorkDay.compare(p.getTime(), q.getTime(), S)) {
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

    public static void resolve(Time p, Time q, int S, Literal l1, Literal l2, boolean isRequired, int penalty) {
        if (!compare(p, q, S)) {
            Utils.addDistributionConstraint(l1, l2, isRequired, penalty);
        }
    }
}
