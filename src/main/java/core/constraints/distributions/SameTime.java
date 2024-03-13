package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class SameTime {
    public static boolean compare(Time i, Time j) {
        // (Ci.start ≤ Cj.start ∧ Cj.end ≤ Ci.end) ∨ (Cj.start ≤ Ci.start ∧ Ci.end ≤ Cj.end)
        if (i.getStart() <= j.getStart() && (j.getEnd() <= i.getEnd())) {
            return true;
        } else {
            return j.getStart() <= i.getStart() && (i.getEnd() <= j.getEnd());
        }
    }
}
