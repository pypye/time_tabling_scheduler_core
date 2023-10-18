package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import core.constraints.defaults.SpecificRoom;
import core.constraints.defaults.SpecificTime;
import entities.Problem;
import entities.courses.Class;
import utils.TimeConvert;


public class Solver {
    private final CpSolver solver = new CpSolver();
    private Problem problem;

    public Solver() {
    }

    public Solver(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void buildModel() {

        for (Class x : Factory.getProblem().getClassList()) {
            x.initSolverConstraint();
        }
        // [class can meet in only specific several room and time]
        for (Class x : Factory.getProblem().getClassList()) {
            SpecificRoom.add(x);
            SpecificTime.add(x);
        }
    }

    public void solve() {
        CpSolverStatus status = solver.solve(Factory.getModel());
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for (Class x : Factory.getProblem().getClassList()) {
                System.out.println(
                    "Class " + x.getId() +
                        " in room " + solver.value(x.room) +
                        " from " + solver.value(x.start) +
                        " to " + solver.value(x.end) +
                        " on day " + TimeConvert.convert(x.day, solver) +
                        " on week " + TimeConvert.convert(x.week, solver)
                );
            }
        } else {
            System.out.println("No solution found!");
        }
        System.out.println(solver.wallTime());
    }
}
