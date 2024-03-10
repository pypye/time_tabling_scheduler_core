package core.solver;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import utils.Random;

public class ConstraintHandler {
    private ConstraintHandler() {
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
