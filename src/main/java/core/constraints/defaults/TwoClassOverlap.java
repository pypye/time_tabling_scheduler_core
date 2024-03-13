package core.constraints.defaults;

import core.constraints.distributions.Overlap;
import core.constraints.distributions.SameRoom;
import entities.Time;
import entities.rooms.Room;

public class TwoClassOverlap {
    public static boolean compare(Room r_i, Room r_j, Time t_i, Time t_j) {
        return SameRoom.compare(r_i, r_j) && Overlap.compare(t_i, t_j);
    }

}
