package core.constraints.defaults;

import core.constraints.distributions.Overlap;
import entities.Time;
import entities.rooms.Room;

public class Unavailable {
    public static boolean compare(Room r, Time t) {
        for (Time u : r.getUnavailableList()) {
            if (Overlap.compare(u, t)) {
                return true;
            }
        }
        return false;
    }
}
