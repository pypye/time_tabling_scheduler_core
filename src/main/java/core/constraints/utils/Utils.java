package core.constraints.utils;

import core.solver.Factory;
import entities.Time;

public class Utils {
    public static String andDays(Time i, Time j) {
        StringBuilder andResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            int temp_i = i.getDays().charAt(x) - '0';
            int temp_j = j.getDays().charAt(x) - '0';
            andResult.append(temp_i & temp_j);
        }
        return andResult.toString();
    }
    public static String andWeeks(Time i, Time j) {
        StringBuilder andResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            int temp_i = i.getWeek().charAt(x) - '0';
            int temp_j = j.getWeek().charAt(x) - '0';
            andResult.append(temp_i & temp_j);
        }
        return andResult.toString();
    }

    public static String orDays(Time i, Time j) {
        StringBuilder orResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrDays(); x++) {
            int temp_i = i.getDays().charAt(x) - '0';
            int temp_j = j.getDays().charAt(x) - '0';
            orResult.append(temp_i | temp_j);
        }
        return orResult.toString();
    }

    public static String orWeeks(Time i, Time j) {
        StringBuilder orResult = new StringBuilder();
        for (int x = 0; x < Factory.getProblem().getNrWeeks(); x++) {
            int temp_i = i.getWeek().charAt(x) - '0';
            int temp_j = j.getWeek().charAt(x) - '0';
            orResult.append(temp_i | temp_j);
        }
        return orResult.toString();
    }
}
