package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Placement;
import entities.courses.Class;
import entities.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class DifferentRoom {
    public static boolean compare(Room i, Room j) {
        // Ci.room ≠ Cj.room
        return !i.getId().equals(j.getId());
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (DifferentRoom.compare(p.getRoom(), q.getRoom())) {
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
        if (i.getRoomList().isEmpty() || j.getRoomList().isEmpty()) {
            return;
        }
        for (Room r1 : Factory.getProblem().getRooms().values()) {
            if (i.getRooms().get(r1) == null) {
                continue;
            }
            for (Room r2 : Factory.getProblem().getRooms().values()) {
                if (j.getRooms().get(r2) == null) {
                    continue;
                }
                if (!DifferentRoom.compare(r1, r2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.getRooms().get(r1).not(),
                        j.getRooms().get(r2).not()
                    });
                }
            }
        }
    }
}
