package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.courses.Class;
import entities.rooms.Room;

public class SameRoom {
    public static boolean compare(Room i, Room j) {
        // Ci.room = Cj.room
        return i.getId().equals(j.getId());
    }
}
