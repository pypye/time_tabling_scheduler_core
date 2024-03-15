package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
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
        int travel_time = 0;
        if (r_i != null && r_j != null) {
            Travel travel_i_to_j = findTravel(r_i, r_j);
            Travel travel_j_to_i = findTravel(r_j, r_i);

            if (travel_i_to_j != null) {
                travel_time = travel_i_to_j.getValue();
            } else if (travel_j_to_i != null) {
                travel_time = travel_j_to_i.getValue();
            }
        }
        return (t_i.getEnd() + travel_time <= t_j.getStart()) || (t_j.getEnd() + travel_time <= t_i.getStart()) || DifferentDays.compare(t_i, t_j) || DifferentWeeks.compare(t_i, t_j);
    }

    private static Travel findTravel(Room a, Room b) {
        return a.getTravelList().stream().filter(travel -> travel.getRoom().equals(b.getId())).findFirst().orElse(null);
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
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
            i.getPlacements().remove(p);
        }
    }

    public static void resolve(Class i, Class j) {
        for (Placement p : i.getPlacements().keySet()) {
            for (Placement q : j.getPlacements().keySet()) {
                if (!SameAttendees.compare(p.getRoom(), q.getRoom(), p.getTime(), q.getTime())) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.getPlacements().get(p).not(),
                        j.getPlacements().get(q).not()
                    });
                }
            }
        }
    }
}
