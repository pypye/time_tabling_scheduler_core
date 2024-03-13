package core.constraints.distributions;

import entities.Time;

public class WorkDay {
    public static boolean compare(Time i, Time j, int S) {
        // ((Ci.days and Cj.days) = 0) ∨ ((Ci.weeks and Cj.weeks) = 0) ∨ (max(Ci.end,Cj.end)−min(Ci.start,Cj.start) ≤ S)
        return DifferentDays.compare(i, j)
            || DifferentWeeks.compare(i, j)
            || Math.max(i.getEnd(), j.getEnd()) - Math.min(i.getStart(), j.getStart()) <= S;
    }
}
