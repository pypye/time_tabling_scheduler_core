package core.constraints.distributions;

import entities.Placement;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import entities.rooms.Travel;

import java.util.ArrayList;
import java.util.List;

public class SameAttendees {
    public static boolean compare(Room r_i, Room r_j, Time t_i, Time t_j) {
        // (Ci.end + Ci.room.travel[Cj.room] ≤ Cj.start) ∨ (Cj.end + Cj.room.travel[Ci.room] ≤ Ci.start) ∨
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0)
        Travel travel_i_to_j = findTravel(r_i, r_j);
        Travel travel_j_to_i = findTravel(r_j, r_i);
        int travel_time = 0;
        if(travel_i_to_j != null) {
            travel_time = travel_i_to_j.getValue();
        } else if(travel_j_to_i != null) {
            travel_time = travel_j_to_i.getValue();
        }
        return (t_i.getEnd() + travel_time <= t_j.getStart()) || (t_j.getEnd() + travel_time <= t_i.getStart()) || DifferentDays.compare(t_i, t_j) || DifferentWeeks.compare(t_i, t_j);
    }

    private static Travel findTravel(Room a, Room b) {
        return a.getTravelList().stream().filter(travel -> travel.getRoom().equals(b.getId())).findFirst().orElse(null);
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Placement p : i.placements) {
            boolean keep = false;
            for (Placement q : j.placements) {
                if (SameAttendees.compare(p.getRoom(), q.getRoom(), p.getTime(), q.getTime())) {
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
