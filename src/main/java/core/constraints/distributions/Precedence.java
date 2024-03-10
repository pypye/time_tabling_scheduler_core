package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class Precedence {
    public static boolean compare(Time i, Time j) {
        // (first(Ci.weeks) < first(Cj.weeks)) ∨
        // [ (first(Ci.weeks) = first(Cj.weeks)) ∧
        //   [ (first(Ci .days) < first(Cj .days)) ∨
        //     ((first(Ci.days) = first(Cj.days)) ∧ (Ci.end ≤ Cj.start))
        //   ]
        // ]
        return firstWeeks(i) < firstWeeks(j) ||
            ((firstWeeks(i) == firstWeeks(j) &&
                (firstDays(i) < firstDays(j) ||
                    ((firstDays(i) == firstDays(j) && i.getEnd() <= j.getStart()))
                )
            ));
    }

    private static int firstWeeks(Time i) {
        int firstWeek = Factory.getProblem().getNrWeeks() + 1;
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            if (i.getWeek().charAt(x) == '1') {
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

    public static void add(Class i, Class j) {
        for (int k = 0; k < i.getAvailableTimeList().size(); k++) {
            Time t1 = i.getAvailableTimeList().get(k);
            for (int l = 0; l < j.getAvailableTimeList().size(); l++) {
                Time t2 = j.getAvailableTimeList().get(l);
                if (!Precedence.compare(t1, t2)) {
                    Factory.getModel().addBoolOr(new Literal[]{
                        i.time[k].not(), j.time[l].not()
                    });
                }
            }
        }
    }
}
