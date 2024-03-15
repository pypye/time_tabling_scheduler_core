package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.constraints.utils.Utils;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class SameWeeks {

    public static boolean compare(Time i, Time j) {
        // (Ci.weeks or Cj.weeks) = Ci.weeks) ∨ (Ci.weeks or Cj.weeks) = Cj.weeks)
        String orResult = Utils.orWeeks(i, j);
        return orResult.equals(i.getWeeks()) || orResult.equals(j.getWeeks());
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (SameWeeks.compare(p.getTime(), q.getTime())) {
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
        for (Time t1 : Factory.getProblem().getTimes().values()) {
            if (i.getTimes().get(t1) == null) {
                continue;
            }
            for (Time t2 : Factory.getProblem().getTimes().values()) {
                if (j.getTimes().get(t2) == null) {
                    continue;
                }
                if (!SameWeeks.compare(t1, t2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.getTimes().get(t1).not(),
                        j.getTimes().get(t2).not()
                    });
                }
            }
        }
    }
}
