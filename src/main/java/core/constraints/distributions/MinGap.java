package core.constraints.distributions;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;

public class MinGap {
    public static boolean compare(Time i, Time j, int G) {
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0) ∨ (Ci.end + G ≤ Cj.start) ∨ (Cj.end + G ≤ Ci.start)
        return DifferentDays.compare(i, j)
            || DifferentWeeks.compare(i, j)
            || i.getEnd() + G <= j.getStart()
            || j.getEnd() + G <= i.getStart();
    }
}
