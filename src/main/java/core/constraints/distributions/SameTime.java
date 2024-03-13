package core.constraints.distributions;

import entities.Time;

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
