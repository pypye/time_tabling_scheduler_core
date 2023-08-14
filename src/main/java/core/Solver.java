package core;

import com.google.ortools.sat.*;

public class Solver {
    private CpModel model = new CpModel();
    private CpSolver solver = new CpSolver();

    public Solver() {
        IntVar[] queens = new IntVar[8];
        for (int i = 0; i < 8; i++) {
            queens[i] = model.newIntVar(0, 7, "queen" + i);
        }
        model.addAllDifferent(queens);
        LinearExpr[] diag1 = new LinearExpr[8];
        LinearExpr[] diag2 = new LinearExpr[8];
        for(int i = 0; i < 8; i++) {
            diag1[i] = LinearExpr.newBuilder().add(queens[i]).add(i).build();
            diag2[i] = LinearExpr.newBuilder().add(queens[i]).add(-i).build();
        }
        model.addAllDifferent(diag1);
        model.addAllDifferent(diag2);
        SolverCallback cb = new SolverCallback(queens);
        solver.getParameters().setEnumerateAllSolutions(true);
        CpSolverStatus status = solver.solve(model, cb);
        System.out.println(status);
        System.out.println(cb.getSolutionCount());

    }


}
