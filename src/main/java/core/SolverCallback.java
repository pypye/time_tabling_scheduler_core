package core;

import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.IntVar;

public class SolverCallback extends CpSolverSolutionCallback {
    private final IntVar[] variableArr;
    private int solutionCount = 0;

    public SolverCallback(IntVar[] variables) {
        this.variableArr = variables;
    }

    @Override
    public void onSolutionCallback() {
        for (IntVar variable : this.variableArr) {
            System.out.println(variable + " = " + value(variable));
        }
        this.solutionCount++;
    }

    public int getSolutionCount() {
        return this.solutionCount;
    }

}
