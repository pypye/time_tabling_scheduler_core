package core.solver;

import com.google.ortools.sat.CpModel;
import entities.Problem;
import utils.DataLoader;

public class Factory {
    private static CpModel model = null;
    private static Problem problem = null;

    private Factory() {
    }

    public static CpModel getModel() {
        if (model == null) {
            model = new CpModel();
        }
        return model;
    }

    public static Problem getProblem() {
        if (problem == null) {
            DataLoader dataLoader = new DataLoader("src/main/resources/data/wbg-fal10.xml");
            problem = dataLoader.getProblem();
        }
        return problem;
    }
}
