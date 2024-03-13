package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class NotOverlap {
    public static boolean compare(Time i, Time j) {
        // (Ci.end ≤ Cj.start) ∨ (Cj.end ≤ Ci.start) ∨ ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0)
        return i.getEnd() <= j.getStart() || j.getEnd() <= i.getStart() || DifferentDays.compare(i, j) || DifferentWeeks.compare(i, j);
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.placements) {
            boolean keep = false;
            for (Placement q : j.placements) {
                if (NotOverlap.compare(p.getTime(), q.getTime())) {
                    keep = true;
                    break;
                }
            }
            if (!keep) {
                removeList.add(p);
            }
        }
        for (Placement p : removeList) {
            i.placements.remove(p);
        }
    }
}
