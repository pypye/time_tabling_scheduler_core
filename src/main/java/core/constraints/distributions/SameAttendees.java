package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import entities.rooms.Travel;

public class SameAttendees {
    public static boolean compare(Room r_i, Room r_j, Time t_i, Time t_j) {
        // (Ci.end + Ci.room.travel[Cj.room] ≤ Cj.start) ∨ (Cj.end + Cj.room.travel[Ci.room] ≤ Ci.start) ∨
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0)
        Travel travel_i_to_j = findTravel(r_i, r_j);
        Travel travel_j_to_i = findTravel(r_j, r_i);
        int travel_i_to_j_time = travel_i_to_j == null ? 0 : travel_i_to_j.getValue();
        int travel_j_to_i_time = travel_j_to_i == null ? 0 : travel_j_to_i.getValue();
        return (t_i.getEnd() + travel_i_to_j_time <= t_j.getStart()) || (t_j.getEnd() + travel_j_to_i_time <= t_i.getStart()) || DifferentDays.compare(t_i, t_j) || DifferentWeeks.compare(t_i, t_j);
    }

    private static Travel findTravel(Room a, Room b) {
        return a.getTravelList().stream().filter(travel -> travel.getRoom().equals(b.getId())).findFirst().orElse(null);
    }

    public static void add(Class i, Class j) {
        for (int k = 0; k < i.getRoomList().size(); k++) {
            Room r1 = i.getRoomList().get(k);
            for (int l = 0; l < j.getRoomList().size(); l++) {
                Room r2 = j.getRoomList().get(l);
                for (int m = 0; m < i.getAvailableTimeList().size(); m++) {
                    Time t1 = i.getAvailableTimeList().get(m);
                    for (int n = 0; n < j.getAvailableTimeList().size(); n++) {
                        Time t2 = j.getAvailableTimeList().get(n);
                        if (!SameAttendees.compare(r1, r2, t1, t2)) {
                            Factory.getModel().addBoolOr(new Literal[]{
                                i.room[k].not(), j.room[l].not(), i.time[m].not(), j.time[n].not()
                            });
                        }
                    }
                }
            }
        }
    }
}
