package main;

import com.google.ortools.Loader;
import core.solver.Solver;
import entities.Problem;
import utils.DataLoader;

public class Main {
    public Main() {
    }

    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.buildModel();
        solver.solve();
    }

}