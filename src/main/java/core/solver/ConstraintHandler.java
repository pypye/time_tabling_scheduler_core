package core.solver;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import utils.Random;

public class ConstraintHandler {
    private ConstraintHandler() {
    }

    /**
     * Description: (time_i and time_j) = k.
     * k = 0: time_i and time_j are not in the same any time slot
     * k = 1: time_i and time_j are matched one or some time slots
     *
     * @param time_i time slot of class i. Example: 00010001
     * @param time_j time slot of class j. Example: 00010010
     * @return k
     */
    public static Literal addTimeSlotConstraintAnd(Literal[] time_i, Literal[] time_j) {
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

    /**
     * need to test
     * @param time_i time slot of class i. Example: 00010001
     * @param time_j time slot of class j. Example: 00010010
     * @param time_k time slot of class k. Example: 00010010
     * @return Formula: (time_i or time_j) = time_k
     */
    public static Literal addTimeSlotConstrainOr(Literal[] time_i, Literal[] time_j, Literal[] time_k) {
        int numTimes = time_i.length;
        Literal[] sameTimeSlot = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            sameTimeSlot[k] = addConstraint(
                Factory.getModel().addBoolOr(new Literal[]{time_i[k], time_j[k]}),
                Factory.getModel().addBoolAnd(new Literal[]{time_i[k].not(), time_j[k].not()})
            );
        }
        Literal[] equality = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            equality[k] = addConstraint(Factory.getModel().addEquality(sameTimeSlot[k], time_k[k]));
        }
        return addConstraint(Factory.getModel().addBoolAnd(equality));
    }

    /**
     * Description: Add a constraint to the model.
     *
     * @param constraint constraint
     * @return Literal
     */
    public static Literal addConstraint(Constraint constraint) {
        Literal c = Factory.getModel().newBoolVar(Random.getRandomHexString());
        constraint.onlyEnforceIf(c); // c => constraint
        return c;
    }

    /**
     * Description: Add a constraint to the model.
     *
     * @param constraintPositive constraint
     * @param constraintNegative constraint
     * @return Literal
     */
    public static Literal addConstraint(Constraint constraintPositive, Constraint constraintNegative) {
        Literal c = Factory.getModel().newBoolVar(Random.getRandomHexString());
        constraintPositive.onlyEnforceIf(c); // c => constraintPositive
        constraintNegative.onlyEnforceIf(c.not()); // not c => constraintNegative
        return c;
    }
}
