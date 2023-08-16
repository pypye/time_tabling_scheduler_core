package core;

import com.google.ortools.sat.*;
import entities.Problem;
import entities.courses.Class;
import utils.Random;
import utils.TimeConvert;

import java.util.List;

public class Solver {
    private final CpModel model = new CpModel();
    private final CpSolver solver = new CpSolver();
    private IntVar[] room;
    private IntVar[] hour;
    private Literal[][] day;
    private Literal[][] week;
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
        int numHours = problem.getSlotsPerDay();
        int numDays = problem.getNrDays();
        int numWeeks = problem.getNrWeeks();
        room = new IntVar[numClasses];
        hour = new IntVar[numClasses];
        day = new Literal[numClasses][numDays];
        week = new Literal[numClasses][numWeeks];
        for (int i = 0; i < numClasses; i++) {
            room[i] = model.newIntVar(0, numClasses, "room_" + i);
            hour[i] = model.newIntVar(0, numHours, "hour_" + i);
            for (int j = 0; j < numDays; j++) {
                day[i][j] = model.newBoolVar("day_" + i + "_slot_" + j);
            }
            for (int j = 0; j < numWeeks; j++) {
                week[i][j] = model.newBoolVar("week_" + i + "_slot_" + j);
            }
        }

        for (int i = 0; i < numClasses; i++) {
            model.addAtLeastOne(day[i]);
            model.addAtLeastOne(week[i]);
        }

        for (int i = 0; i < numClasses; i++) {
            for (int j = i + 1; j < numClasses; j++) {
                Literal diffRoom = model.newBoolVar("diffRoom_" + i + "_" + j);
                Literal diffHour = model.newBoolVar("diffHour_" + i + "_" + j);
                Literal sameDay = model.newBoolVar("diffDay_" + i + "_" + j);
                Literal sameWeek = model.newBoolVar("diffWeek_" + i + "_" + j);
                model.addDifferent(room[i], room[j]).onlyEnforceIf(diffRoom);
                model.addDifferent(hour[i], hour[j]).onlyEnforceIf(diffHour);

                for (int k = 0; k < numDays; k++) {
                    Literal c = evaluateAnd(day[i][k], day[j][k]);
                    sameDay = evaluateOr(sameDay, c);
                }

                for (int k = 0; k < numWeeks; k++) {
                    Literal c = evaluateAnd(week[i][k], week[j][k]);
                    sameWeek = evaluateOr(sameWeek, c);
                }

                model.addBoolOr(new Literal[]{diffRoom, diffHour, sameDay.not(), sameWeek.not()});
            }
        }
    }

    public void solve() {
        CpSolverStatus status = solver.solve(model);
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for (int i = 0; i < room.length; i++) {
                System.out.println("Class " + i + " in room " + solver.value(room[i]) + " at hour " + solver.value(hour[i]) + " on day " + TimeConvert.convert(day[i], solver) + " on week " + TimeConvert.convert(week[i], solver));
            }
        } else {
            System.out.println("No solution found!");
        }
        System.out.println(solver.wallTime());
    }

    public Literal evaluateOr(Literal a, Literal b) {
        String name = Random.getRandomHexString();
        Literal c = model.newBoolVar(name);
        model.addBoolOr(new Literal[]{a, b}).onlyEnforceIf(c);
        model.addBoolAnd(new Literal[]{a.not(), b.not()}).onlyEnforceIf(c.not());
        return c;
    }

    public Literal evaluateAnd(Literal a, Literal b) {
        String name = Random.getRandomHexString();
        Literal c = model.newBoolVar(name);
        model.addBoolAnd(new Literal[]{a, b}).onlyEnforceIf(c);
        model.addBoolOr(new Literal[]{a.not(), b.not()}).onlyEnforceIf(c.not());
        return c;
    }


}
