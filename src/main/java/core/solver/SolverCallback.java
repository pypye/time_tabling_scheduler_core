package core.solver;

import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.IntVar;

public class SolverCallback extends CpSolverSolutionCallback {
    private final IntVar[] room;
    private final IntVar[] hour;
    private int solutionCount = 0;

    public SolverCallback(IntVar[] room, IntVar[] hour) {
        this.room = room;
        this.hour = hour;
    }

    @Override
    public void onSolutionCallback() {
        for(int i = 0; i < room.length; i++) {
            System.out.println("Class " + i + " is in room " + this.value(room[i]) + " at hour " + this.value(hour[i]));
        }
        this.solutionCount++;
    }

    public int getSolutionCount() {
        return this.solutionCount;
    }

}
