package core.constraints.defaults;

import com.google.ortools.sat.Literal;
import core.constraints.distributions.Overlap;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;

public class UnavailableRoom {

    public static void add(Class x) {
        // (¬Ri v ¬Ti)
        for (int i = 0; i < x.getRoomList().size(); i++) {
            Room r = x.getRoomList().get(i);
            for (int j = 0; j < r.getUnavailableList().size(); j++) {
                Time t = r.getUnavailableList().get(j);
                for (int k = 0; k < x.getAvailableTimeList().size(); k++) {
                    Time t2 = x.getAvailableTimeList().get(k);
                    if (Overlap.compare(t, t2)) {
                        Factory.getModel().addBoolOr(new Literal[]{x.room[i].not(), x.time[k].not()});
                    }
                }
            }
        }
    }
}
