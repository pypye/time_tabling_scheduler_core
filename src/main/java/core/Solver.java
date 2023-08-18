package core;

import com.google.ortools.sat.*;
import entities.Problem;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import utils.Random;
import utils.TimeConvert;

import java.util.List;

public class Solver {
    private final CpModel model = new CpModel();
    private final CpSolver solver = new CpSolver();
    private final SolverConstraint constraint = new SolverConstraint(model);
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
        int numRooms = problem.getRoomList().size();
        int numHours = problem.getSlotsPerDay();
        int numDays = problem.getNrDays();
        int numWeeks = problem.getNrWeeks();
        room = new IntVar[numClasses];
        hour = new IntVar[numClasses];
        day = new Literal[numClasses][numDays];
        week = new Literal[numClasses][numWeeks];
        for (int i = 0; i < numClasses; i++) {
            room[i] = model.newIntVar(1, numRooms, "room_" + i);
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
        // [class cannot be overlap]
        for (int i = 0; i < numClasses; i++) {
            for (int j = i + 1; j < numClasses; j++) {
                Literal diffRoom = constraint.addConstraint(model.addDifferent(room[i], room[j]));
                Literal diffHour = constraint.addConstraint(model.addDifferent(hour[i], hour[j]));
                Literal sameDay = constraint.addTimeSlotConstraint(i, j, day);
                Literal sameWeek = constraint.addTimeSlotConstraint(i, j, week);
                model.addBoolOr(new Literal[]{diffRoom, diffHour, sameDay.not(), sameWeek.not()});
            }
        }
        // [class can meet in only specific several room]
        for (int i = 0; i < numClasses; i++) {
            List<Room> roomList = classList.get(i).getRoomList();
            Literal[] c = new Literal[roomList.size()];
            for (int j = 0; j < roomList.size(); j++) {
                int roomId = Integer.parseInt(roomList.get(j).getId()) - 1;
                c[j] = constraint.addConstraint(model.addEquality(this.room[i], roomId));
            }
            model.addBoolOr(c);
        }
        // [class can meet in only specific several time]
        for (int i = 0; i < numClasses; i++) {
            List<Time> timeList = classList.get(i).getAvailableTimeList();
            Literal[] c = new Literal[timeList.size()];
            for (int j = 0; j < timeList.size(); j++) {
                int hour = timeList.get(j).getStart();
                c[j] = model.newBoolVar(Random.getRandomHexString());
                Long[] days = convertFromString(timeList.get(j).getDays());
                Long[] weeks = convertFromString(timeList.get(j).getWeek());
                Literal sameHour = constraint.addConstraint(model.addEquality(this.hour[i], hour));
                Literal[] sameDaySlot = new Literal[numDays];
                Literal[] sameWeekSlot = new Literal[numWeeks];
                for (int k = 0; k < numDays; k++) {
                    sameDaySlot[k] = constraint.addConstraint(model.addEquality(day[i][k], days[k]));
                }
                Literal sameDay = constraint.addConstraint(model.addBoolAnd(sameDaySlot));
                for (int k = 0; k < numWeeks; k++) {
                    sameWeekSlot[k] = constraint.addConstraint(model.addEquality(week[i][k], weeks[k]));
                }
                Literal sameWeek = constraint.addConstraint(model.addBoolAnd(sameWeekSlot));
                c[j] = constraint.addConstraint(model.addBoolAnd(new Literal[]{sameHour, sameDay, sameWeek}));
            }
            model.addBoolOr(c);
        }
    }

    public void solve() {
        CpSolverStatus status = solver.solve(model);
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            for (int i = 0; i < room.length; i++) {
                System.out.println("Class " + problem.getClassList().get(i).getId() + " in room " + problem.getRoomList().get((int) solver.value(room[i])).getId() + " at hour " + solver.value(hour[i]) + " on day " + TimeConvert.convert(day[i], solver) + " on week " + TimeConvert.convert(week[i], solver));
            }
        } else {
            System.out.println("No solution found!");
        }
        System.out.println(solver.wallTime());
    }


    public static Long[] convertFromString(String timeSlot) {
        Long[] result = new Long[timeSlot.length()];
        for (int i = 0; i < timeSlot.length(); i++) {
            result[i] = timeSlot.charAt(i) == '1' ? 1L : 0L;
        }
        return result;
    }
}
