package core.constraints.defaults;

import com.google.ortools.sat.Literal;
import core.constraints.distributions.Overlap;
import core.constraints.distributions.SameRoom;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;

import java.util.ArrayList;

public class TwoClassNotOverlap {

    public static int add(Class i, Class j) {
        // (¬Ri v ¬Rj v ¬Ti v ¬Tj)
        int count = 0;
        for (int k = 0; k < i.getRoomList().size(); k++) {
            for (int l = 0; l < j.getRoomList().size(); l++) {
                Room r1 = i.getRoomList().get(k);
                Room r2 = j.getRoomList().get(l);
                // (-a v -b) ^ (-a v -c) ^ (-a v -d) = -a v (-b ^ -c ^ -d)
                if (SameRoom.compare(r1, r2)) {
                    for (int m = 0; m < i.getAvailableTimeList().size(); m++) {
                        Time t1 = i.getAvailableTimeList().get(m);
                        if (!checkOverlap(t1, r1)) {
                            continue;
                        }
                        for (int n = 0; n < j.getAvailableTimeList().size(); n++) {
                            Time t2 = j.getAvailableTimeList().get(n);
                            if(!checkOverlap(t2, r2)){
                                continue;
                            }
                            if (Overlap.compare(t1, t2)) {
                                count++;
                                Factory.getModel().addBoolOr(new Literal[]{
                                    i.room[k].not(), j.room[l].not(), i.time[m].not(), j.time[n].not()
                                });
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private static boolean checkOverlap(Time t1, Room r1) {
        boolean isUnavailable = true;
        for (int x = 0; x < r1.getUnavailableList().size(); x++) {
            Time x1 = r1.getUnavailableList().get(x);
            if (Overlap.compare(t1, x1)) {
                isUnavailable = false;
                break;
            }
        }
        return isUnavailable;
    }
}
