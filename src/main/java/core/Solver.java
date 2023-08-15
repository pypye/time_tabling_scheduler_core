package core;

import com.google.ortools.sat.*;
import entities.Problem;
import entities.courses.Class;

import java.util.List;

public class Solver {
    private CpModel model = new CpModel();
    private CpSolver solver = new CpSolver();
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
        IntVar[] room = new IntVar[classList.size()];
        IntVar[] hour = new IntVar[classList.size()];
        IntVar[] day = new IntVar[classList.size()];
        IntVar[] week = new IntVar[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            room[i] = model.newIntVar(0, problem.getRoomList().size() - 1, "room" + i);
            hour[i] = model.newIntVar(0, problem.getSlotsPerDay(), "hour" + i);
            day[i] = model.newIntVar(0, 1L << problem.getNrDays(), "day" + i);
            week[i] = model.newIntVar(0, 1L << problem.getNrWeeks(), "week" + i);
        }
        for (int i = 0; i < classList.size(); i++) {
            for(int j = i + 1; j < classList.size(); j++) {
                model.addAllDifferent(new IntVar[]{room[i], room[j]});
                model.addAllDifferent(new IntVar[]{hour[i], hour[j]});
            }
        }
    }

    public void solve() {
        System.out.println("Solving...");
        System.out.println("Solved!");
    }
}
