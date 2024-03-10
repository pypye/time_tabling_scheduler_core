package core.constraints.distributions;

import core.solver.Factory;
import entities.Time;

import java.util.ArrayList;

public class MaxDays {

    public static boolean compare(ArrayList<Time> times, int D) {
        // countNonzeroBits(C1.days or C2.days or ⋅ ⋅ ⋅ Cn.days) ≤ D
        int count = 0;
        StringBuilder orResult = new StringBuilder();
        orResult.append("0".repeat(Math.max(0, Factory.getProblem().getNrDays())));
        for (Time time : times) {
            orResult = new StringBuilder(orDays(orResult.toString(), time.getDays()));
        }

        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            if (orResult.charAt(x) == '1') {
                count++;
            }
        }
        return count <= D;
    }

    private static String orDays(String i, String j) {
        StringBuilder orResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            int temp_i = i.charAt(x) - '0';
            int temp_j = j.charAt(x) - '0';
            orResult.append((temp_i | temp_j) + '0');
        }
        return orResult.toString();
    }
}
