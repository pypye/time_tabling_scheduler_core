package core.constraints.defaults;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;
import utils.Random;
import utils.StringFormatter;

import java.util.List;

public class SpecificTime {

    /**
     * <p>Description: Class can meet in only specific several time
     * <p>Formula:
     * <p>time_i = [(x.start = start_i) ∧ (x.end = end_i) ∧ (x.day = days_i) ∧ (x.week = weeks_i)]
     * <p>F = time_0 ∨ time_1 ∨ ... ∨ time_n
     *
     * @param x Class
     */
    public static void add(Class x) {
        List<Time> timeList = x.getAvailableTimeList();
        Literal[] c = new Literal[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            int start = timeList.get(i).getStart();
            int end = timeList.get(i).getEnd();
            c[i] = Factory.getModel().newBoolVar(Random.getRandomHexString());
            Long[] days = StringFormatter.convertFromString(timeList.get(i).getDays());
            Long[] weeks = StringFormatter.convertFromString(timeList.get(i).getWeek());
            Literal sameStart = ConstraintHandler.addConstraint(Factory.getModel().addEquality(x.start, start)); // (x.start = start_i)
            Literal sameEnd = ConstraintHandler.addConstraint(Factory.getModel().addEquality(x.end, end)); // (x.end = end_i)
            Literal sameDay = sameTimeSlot(x.day, days, Factory.getProblem().getNrDays()); // (x.day = days_i)
            Literal sameWeek = sameTimeSlot(x.week, weeks, Factory.getProblem().getNrWeeks()); // (x.week = weeks_i)

            c[i] = ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(new Literal[]{sameStart, sameEnd, sameDay, sameWeek})); // time_i = [(x.start = start_i) ∧ (x.end = end_i) ∧ (x.day = days_i) ∧ (x.week = weeks_i)]
        }
        Factory.getModel().addBoolOr(c); // F = time_0 ∨ time_1 ∨ ... ∨ time_n
    }

    private static Literal sameTimeSlot(Literal[] time, Long[] matchingTime, int length) {
        Literal[] sameTimeSlot = new Literal[length];
        for (int i = 0; i < length; i++) {
            sameTimeSlot[i] = ConstraintHandler.addConstraint(Factory.getModel().addEquality(time[i], matchingTime[i]));
        }
        return ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(sameTimeSlot));
    }
}
