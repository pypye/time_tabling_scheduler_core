package core.constraints.defaults;

import core.constraints.distributions.Overlap;
import core.constraints.distributions.SameAttendees;
import core.constraints.distributions.SameRoom;
import entities.Placement;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class TwoClassOverlap {
    public static boolean compare(Room r_i, Room r_j, Time t_i, Time t_j) {
        return SameRoom.compare(r_i, r_j) && Overlap.compare(t_i, t_j);
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.placements) {
            boolean keep = false;
            for (Placement q : j.placements) {
                if (TwoClassOverlap.compare(p.getRoom(), q.getRoom(), p.getTime(), q.getTime())) {
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
