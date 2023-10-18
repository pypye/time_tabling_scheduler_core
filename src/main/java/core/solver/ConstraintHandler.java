package core.solver;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.Literal;
import utils.Random;

public class ConstraintHandler {
    private ConstraintHandler() {
    }

    public static Literal addTimeSlotConstraint(Literal[] time_i, Literal[] time_j) {
        int numTimes = time_i.length;
        Literal[] sameTimeSlot = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            sameTimeSlot[k] = addConstraint(
                Factory.getModel().addBoolAnd(new Literal[]{time_i[k], time_j[k]}),
                Factory.getModel().addBoolOr(new Literal[]{time_i[k].not(), time_j[k].not()})
            );
        }
        Literal[] negativeTimeSlot = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            negativeTimeSlot[k] = sameTimeSlot[k].not();
        }
        return addConstraint(Factory.getModel().addBoolOr(sameTimeSlot), Factory.getModel().addBoolAnd(negativeTimeSlot));
    }

    public static Literal addConstraint(Constraint constraint) {
        Literal c = Factory.getModel().newBoolVar(Random.getRandomHexString());
        constraint.onlyEnforceIf(c);
        return c;
    }

    public static Literal addConstraint(Constraint constraintPositive, Constraint constraintNegative) {
        Literal c = Factory.getModel().newBoolVar(Random.getRandomHexString());
        constraintPositive.onlyEnforceIf(c);
        constraintNegative.onlyEnforceIf(c.not());
        return c;
    }
}
