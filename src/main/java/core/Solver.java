package core;

import com.google.ortools.sat.*;
import entities.Problem;
import entities.courses.Class;

import java.util.List;

public class Solver {
    private final CpModel model = new CpModel();
    private final CpSolver solver = new CpSolver();
    private IntVar[] room;
    private IntVar[] hour;
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
        List<Class> classList = problem.getClassList();
        int numClasses = classList.size();
        int numRooms = problem.getRoomList().size();
        int numHours = problem.getSlotsPerDay();
        room = new IntVar[numClasses];
        hour = new IntVar[numClasses];
        for (int i = 0; i < numClasses; i++) {
            room[i] = model.newIntVar(0, numRooms - 1, "room[" + i + "]");
        }
        for (int i = 0; i < numClasses; i++) {
            hour[i] = model.newIntVar(0, numHours - 1, "hour[" + i + "]");
        }
        // All classes must be in different rooms or (same room at different hour)
        for(int i = 0; i < numClasses; i++) {
            for(int j = i + 1; j < numClasses; j++) {
                BoolVar diffRoom = model.newBoolVar("diffRoom[" + i + "," + j + "]");
                BoolVar diffHour = model.newBoolVar("diffHour[" + i + "," + j + "]");
                model.addDifferent(room[i], room[j]).onlyEnforceIf(diffRoom);
                model.addDifferent(hour[i], hour[j]).onlyEnforceIf(diffHour);
                model.addBoolOr(new BoolVar[]{diffRoom, diffHour});
            }
        }
    }

    public void solve() {
        CpSolverStatus status = solver.solve(model);
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for(int i = 0; i < room.length; i++) {
                System.out.println("Class " + i + " is in room " + solver.value(room[i]) + " at hour " + solver.value(hour[i]));
            }
        }
        System.out.println(solver.wallTime());
    }
}
