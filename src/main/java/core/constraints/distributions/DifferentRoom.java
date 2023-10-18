package core.constraints.distributions;

import core.solver.Factory;
import entities.courses.Class;

public class DifferentRoom {
    public static void add(Class i, Class j) {
        Factory.getModel().addDifferent(i.room, j.room);
    }

}
