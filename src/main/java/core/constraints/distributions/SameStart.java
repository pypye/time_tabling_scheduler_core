package core.constraints.distributions;

import core.solver.Factory;
import entities.courses.Class;

public class SameStart {
    public static void add(Class i, Class j) {
        Factory.getModel().addEquality(i.start, j.start);
    }
}
