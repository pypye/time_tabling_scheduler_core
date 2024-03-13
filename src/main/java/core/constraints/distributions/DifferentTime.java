package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class DifferentTime {
    public static boolean compare(Time i, Time j) {
        // (Ci.end ≤ Cj.start) ∨ (Cj.end ≤ Ci.start)
        return i.getEnd() <= j.getStart() || j.getEnd() <= i.getStart();
    }
}
