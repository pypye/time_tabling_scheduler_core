package core.constraints.distributions;

import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import core.solver.ConstraintHandler;
import core.solver.Factory;
import entities.courses.Class;
import entities.rooms.Room;
import entities.rooms.Travel;

import java.util.ArrayList;

public class SameAttendees {
    public static void add(Class i, Class j) {
        ArrayList<Literal> attendList = new ArrayList<>();
        for (Room x : i.getRoomList()) {
            for (Room y : j.getRoomList()) {
                if (i.room == null || j.room == null) {
                    continue;
                }
                Literal roomIEqX = ConstraintHandler.addConstraint(Factory.getModel().addEquality(i.room, Integer.parseInt(x.getId())));
                Literal roomIJEqY = ConstraintHandler.addConstraint(Factory.getModel().addEquality(j.room, Integer.parseInt(y.getId())));

                Travel firstTravel = findTravel(x, y); // x.travel[y]
                int travelTime1 = firstTravel != null ? firstTravel.getValue() : 0;
                LinearExprBuilder firstAttendeeExpr = LinearExpr.newBuilder().add(i.end).add(travelTime1);
                Literal firstAttendee = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(firstAttendeeExpr.build(), j.start)); // Ci.end + Ci.room.travel[Cj.room] ≤ Cj.start

                Travel secondTravel = findTravel(y, x); // y.travel[x]
                int travelTime2 = secondTravel != null ? secondTravel.getValue() : 0;
                LinearExprBuilder secondAttendeeExpr = LinearExpr.newBuilder().add(j.end).add(travelTime2);
                Literal secondAttendee = ConstraintHandler.addConstraint(Factory.getModel().addLessOrEqual(secondAttendeeExpr.build(), i.start)); // Cj.end + Cj.room.travel[Ci.room] ≤ Ci.start

                Literal diffDay = ConstraintHandler.addTimeSlotConstraintAnd(i.day, j.day).not(); // (c_i.days and c_j.days) = 0
                Literal diffWeek = ConstraintHandler.addTimeSlotConstraintAnd(i.week, j.week).not(); // (c_i.weeks and c_j.weeks) = 0

                Literal sameAttendees = ConstraintHandler.addConstraint(Factory.getModel().addBoolOr(new Literal[]{firstAttendee, secondAttendee, diffDay, diffWeek}));
                Literal sameAttendeesWithRoom = ConstraintHandler.addConstraint(Factory.getModel().addBoolAnd(new Literal[]{roomIEqX, roomIJEqY, sameAttendees}));
                attendList.add(sameAttendeesWithRoom);
            }
        }
        Factory.getModel().addBoolOr(attendList.toArray(new Literal[0]));
    }


    private static Travel findTravel(Room a, Room b) {
        return a.getTravelList().stream().filter(travel -> travel.getRoom().equals(b.getId())).findFirst().orElse(null);
    }
}
