package core.constraints.distributions;

import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class SameStart {
    public static boolean compare(Time i, Time j) {
        // Ci.start = Cj.start
        return i.getStart() == j.getStart();
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.placements) {
            boolean keep = false;
            for (Placement q : j.placements) {
                if (SameStart.compare(p.getTime(), q.getTime())) {
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
