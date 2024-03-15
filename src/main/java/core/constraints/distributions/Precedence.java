package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Placement;
import entities.Time;
import entities.courses.Class;

import java.util.ArrayList;
import java.util.List;

public class Precedence {
    public static boolean compare(Time i, Time j) {
        // (first(Ci.weeks) < first(Cj.weeks)) ∨
        // [ (first(Ci.weeks) = first(Cj.weeks)) ∧
        //   [ (first(Ci.days) < first(Cj.days)) ∨
        //     ((first(Ci.days) = first(Cj.days)) ∧ (Ci.end ≤ Cj.start))
        //   ]
        // ]
        return (firstWeeks(i) < firstWeeks(j)) || ((firstWeeks(i) == firstWeeks(j)) && ((firstDays(i) < firstDays(j)) || ((firstDays(i) == firstDays(j)) && i.getEnd() <= j.getStart())));
    }

    private static int firstWeeks(Time i) {
        int firstWeek = Factory.getProblem().getNrWeeks() + 1;
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            if (i.getWeeks().charAt(x) == '1') {
                firstWeek = x;
                break;
            }
        }
        return firstWeek;
    }

    private static int firstDays(Time i) {
        int firstDay = Factory.getProblem().getNrDays() + 1;
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            if (i.getDays().charAt(x) == '1') {
                firstDay = x;
                break;
            }
        }
        return firstDay;
    }

    public static void remove(Class i, Class j) {
        List<Placement> removeList = new ArrayList<>();
        for (Placement p : i.getPlacements().keySet()) {
            boolean keep = false;
            for (Placement q : j.getPlacements().keySet()) {
                if (Integer.parseInt(i.getId()) < Integer.parseInt(j.getId()) && Precedence.compare(p.getTime(), q.getTime())) {
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
                if (!Precedence.compare(t1, t2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.getTimes().get(t1).not(),
                        j.getTimes().get(t2).not()
                    });
                }
            }
        }
    }
}
