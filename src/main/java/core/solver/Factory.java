package core.solver;

import com.google.ortools.sat.CpModel;
import entities.Problem;
import utils.DataLoader;
import utils.Log;

public class Factory {
    private static CpModel model = null;
    private static Problem problem = null;

    private Factory() {
    }

    public static void init(String path) {
        if (model == null) {
            model = new CpModel();
        }
        if (problem == null) {
            problem = new Problem();
            DataLoader.parse(path);
            Log.info("Problem parsed: path=" + path);
        }
    }

    public static CpModel getModel() {
        return model;
    }

    public static Problem getProblem() {
        return problem;
    }
}
