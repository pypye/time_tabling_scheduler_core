package core.constraints.distributions;

import entities.Time;

public class DifferentTime {
    public static boolean compare(Time i, Time j) {
        // (Ci.end ≤ Cj.start) ∨ (Cj.end ≤ Ci.start)
        return i.getEnd() <= j.getStart() || j.getEnd() <= i.getStart();
    }
}
