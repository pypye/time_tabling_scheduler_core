package core.constraints.defaults;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;
import entities.rooms.Room;

import java.util.List;

public class SpecificRoom {
    /**
     * <p>Description: Class can meet in only specific several room
     * <p>Formula: (x.room = room_0) ∨ (x.room = room_1) ∨ ... ∨ (x.room = room_n)
     *
     * @param x Class
     */
    public static void add(Class x) {
        List<Room> roomList = x.getRoomList();
        Literal[] c = new Literal[roomList.size()];
        for (int j = 0; j < roomList.size(); j++) {
            int roomId = Integer.parseInt(roomList.get(j).getId()) - 1;
            c[j] = ConstraintHandler.addConstraint(Factory.getModel().addEquality(x.room, roomId)); // (x.room = room_j)
        }
        Factory.getModel().addBoolOr(c); // (x.room = room_0) ∨ (x.room = room_1) ∨ ... ∨ (x.room = room_n)
    }
}
