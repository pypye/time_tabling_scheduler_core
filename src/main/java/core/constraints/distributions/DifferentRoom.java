package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.courses.Class;
import entities.rooms.Room;

public class DifferentRoom {
    public static boolean compare(Room i, Room j) {
        // Ci.room ≠ Cj.room
        return !i.getId().equals(j.getId());
    }

    public static void add(Class i, Class j) {
        for (int k = 0; k < i.getRoomList().size(); k++) {
            Room r1 = i.getRoomList().get(k);
            for (int l = 0; l < j.getRoomList().size(); l++) {
                Room r2 = j.getRoomList().get(l);
                if (!DifferentRoom.compare(r1, r2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.room[k].not(), j.room[l].not()
                    });
                }
            }
        }
    }
}
