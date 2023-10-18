package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import core.constraints.NotOverlap;
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

        // [class cannot be overlap]
        for (Class x : Factory.getProblem().getClassList()) {
            for (Class y : Factory.getProblem().getClassList()) {
                if (x.getId().equals(y.getId())) {
                    continue;
                }
                NotOverlap.add(x, y);
            }
        }
//        // [class can meet in only specific several room]
//        for (int i = 0; i < numClasses; i++) {
//            List<Room> roomList = classList.get(i).getRoomList();
//            Literal[] c = new Literal[roomList.size()];
//            for (int j = 0; j < roomList.size(); j++) {
//                int roomId = Integer.parseInt(roomList.get(j).getId()) - 1;
//                c[j] = constraintHandler.addConstraint(model.addEquality(this.room[i], roomId));
//            }
//            model.addBoolOr(c);
//        }
//        // [class can meet in only specific several time]
//        for (int i = 0; i < numClasses; i++) {
//            List<Time> timeList = classList.get(i).getAvailableTimeList();
//            Literal[] c = new Literal[timeList.size()];
//            for (int j = 0; j < timeList.size(); j++) {
//                int hour = timeList.get(j).getStart();
//                c[j] = model.newBoolVar(Random.getRandomHexString());
//                Long[] days = StringFormatter.convertFromString(timeList.get(j).getDays());
//                Long[] weeks = StringFormatter.convertFromString(timeList.get(j).getWeek());
//                Literal sameHour = constraintHandler.addConstraint(model.addEquality(this.hour[i], hour));
//                Literal[] sameDaySlot = new Literal[numDays];
//                Literal[] sameWeekSlot = new Literal[numWeeks];
//                for (int k = 0; k < numDays; k++) {
//                    sameDaySlot[k] = constraintHandler.addConstraint(model.addEquality(day[i][k], days[k]));
//                }
//                Literal sameDay = constraintHandler.addConstraint(model.addBoolAnd(sameDaySlot));
//                for (int k = 0; k < numWeeks; k++) {
//                    sameWeekSlot[k] = constraintHandler.addConstraint(model.addEquality(week[i][k], weeks[k]));
//                }
//                Literal sameWeek = constraintHandler.addConstraint(model.addBoolAnd(sameWeekSlot));
//                c[j] = constraintHandler.addConstraint(model.addBoolAnd(new Literal[]{sameHour, sameDay, sameWeek}));
//            }
//            model.addBoolOr(c);
//        }
    }

    public void solve() {
        CpSolverStatus status = solver.solve(Factory.getModel());
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for (Class x : Factory.getProblem().getClassList()) {
                System.out.println(
                    "Class " + x.getId() +
                        " in room " + solver.value(x.room) +
                        " at hour " + solver.value(x.hour) +
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
