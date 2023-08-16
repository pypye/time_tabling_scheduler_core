package main;

import com.google.ortools.Loader;
import core.Solver;
import entities.Problem;
import utils.DataLoader;

public class Main {
    public Main() {
    }

    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader("src/main/resources/data/lums-sum17.xml");
        Problem problem = dataLoader.getProblem();
        Solver solver = new Solver(problem);
        solver.buildModel();
        solver.solve();
    }

}