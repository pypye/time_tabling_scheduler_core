package core.constraints.defaults;

import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import utils.StringFormatter;

public class UnavailableRoom {

    public static void add(Class x, Room y, Time z) {
        Long[] days = StringFormatter.convertFromString(z.getDays());
        Long[] weeks = StringFormatter.convertFromString(z.getWeek());
        Literal diffRoom = ConstraintHandler.addConstraint(Factory.getModel().addDifferent(x.room, Integer.parseInt(y.getId()))); // (x.room ≠ y)
        Literal diffTime1 = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(x.end, z.getStart())); // (c_i.end ≤ c_j.start)
        Literal diffTime2 = ConstraintHandler.addConstraint(Factory.getModel().addGreaterThan(x.start, z.getEnd())); // (c_i.start > c_j.end)
        Literal diffDay = diffTimeSlot(x.day, days); // (c_i.days and c_j.days) = 0
        Literal diffWeek = diffTimeSlot(x.week, weeks); // (c_i.weeks and c_j.weeks) = 0
        Factory.getModel().addBoolOr(new Literal[]{diffRoom, diffTime1, diffTime2, diffDay, diffWeek});
    }

    public static Literal diffTimeSlot(Literal[] time, Long[] matchingTime) {
        Literal[] sameTimeSlot = new Literal[matchingTime.length];
        for (int i = 0; i < matchingTime.length; i++) {
            if (matchingTime[i] == 0) {
                // add an always true constraint
                sameTimeSlot[i] = ConstraintHandler.addConstraint(Factory.getModel().addEquality(time[i], time[i]));
            } else {
                sameTimeSlot[i] = ConstraintHandler.addConstraint(Factory.getModel().addDifferent(time[i], matchingTime[i]));
            }
        }
        return ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(sameTimeSlot));
    }
}
