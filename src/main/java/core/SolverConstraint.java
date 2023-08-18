package core;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.Literal;
import utils.Random;

public class SolverConstraint {
    private CpModel model;

    public SolverConstraint() {
    }

    public SolverConstraint(CpModel model) {
        this.model = model;
    }

    public Literal addTimeSlotConstraint(int i, int j, Literal[][] time) {
        int numTimes = time[i].length;
        Literal[] sameTimeSlot = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            sameTimeSlot[k] = addConstraint(
                model.addBoolAnd(new Literal[]{time[i][k], time[j][k]}),
                model.addBoolOr(new Literal[]{time[i][k].not(), time[j][k].not()})
            );
        }
        Literal[] negativeTimeSlot = new Literal[numTimes];
        for (int k = 0; k < numTimes; k++) {
            negativeTimeSlot[k] = sameTimeSlot[k].not();
        }
        return addConstraint(
            model.addBoolOr(sameTimeSlot),
            model.addBoolAnd(negativeTimeSlot)
        );
    }

    public Literal addConstraint(Constraint constraint) {
        Literal c = model.newBoolVar(Random.getRandomHexString());
        constraint.onlyEnforceIf(c);
        return c;
    }

    public Literal addConstraint(Constraint constraintPositive, Constraint constraintNegative) {
        Literal c = model.newBoolVar(Random.getRandomHexString());
        constraintPositive.onlyEnforceIf(c);
        constraintNegative.onlyEnforceIf(c.not());
        return c;
    }
}
