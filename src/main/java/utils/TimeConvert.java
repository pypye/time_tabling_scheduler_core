package utils;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.Literal;

public class TimeConvert {
    public static long encode(String time) {
        long result = 0;
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) == '1') {
                result += 1L << i;
            }
        }
        return result;
    }

    public static String decode(long number, int maxLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            result.append((number & (1L << i)) != 0 ? "1" : "0");
        }
        return result.toString();
    }

    public static String decode(long number) {
        return TimeConvert.decode(number, 64);
    }

    public static String convert(Literal[] timeSlot, CpSolver solver) {
        StringBuilder result = new StringBuilder();
        for (Literal literal : timeSlot) {
            result.append(solver.value(literal) == 1 ? "1" : "0");
        }
        return result.toString();
    }
}
