package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import core.constraints.defaults.SpecificRoom;
import core.constraints.defaults.SpecificTime;
import core.constraints.defaults.TwoClassNotOverlap;
import core.constraints.defaults.UnavailableRoom;
import entities.Problem;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
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
        // init solver constraint
        for (Class x : Factory.getProblem().getClassList()) {
            x.initSolverConstraint();
        }
        // class can meet in only specific several room and time
        for (Class x : Factory.getProblem().getClassList()) {
            SpecificRoom.add(x);
            SpecificTime.add(x);
        }
        // 2 class can not overlap in time and room
        for (Class x : Factory.getProblem().getClassList()) {
            for(Class y : Factory.getProblem().getClassList()) {
                if(x.getId().equals(y.getId())) continue;
                TwoClassNotOverlap.add(x, y);
            }
        }
        // class can not meet in unavailable room
        for(Class x : Factory.getProblem().getClassList()) {
            for(Room y : x.getRoomList()) {
                for(Time z : x.getAvailableTimeList()) {
                    UnavailableRoom.add(x, y, z);
                }
            }
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
