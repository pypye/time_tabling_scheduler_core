package main;

import com.google.ortools.Loader;
import core.solver.Factory;
import core.solver.Solver;

public class Main {
    public Main() {
    }

    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
        String path = "src/main/resources/data/wbg-fal10.xml";
        Factory.init(path);
        Solver solver = new Solver();
        solver.buildModel();
        solver.solve();
    }

}