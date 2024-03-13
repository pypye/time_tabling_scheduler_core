package core.solver;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import core.constraints.distributions.Overlap;
import core.constraints.distributions.SameRoom;
import entities.Placement;
import entities.Problem;
import utils.DataLoader;

import java.util.*;

public class Factory {
    private static CpModel model = null;
    private static Problem problem = null;

    private static final Set<Placement> placements = new HashSet<>();

    private static final Map<Placement, ArrayList<Literal>> placementConflicts = new HashMap<>();

    private static final Map<Placement, Literal> placementLiterals = new HashMap<>();

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
            System.out.println("Loading problem from file");
            DataLoader dataLoader = new DataLoader("src/main/resources/data/lums-spr18.xml");
            System.out.println("Problem loaded");
            problem = dataLoader.getProblem();
        }
        return problem;
    }

    public static Set<Placement> getPlacements() {
        return placements;
    }

    public static Map<Placement, Literal> getPlacementLiterals() {
        return placementLiterals;
    }

    public static Map<Placement, ArrayList<Literal>> getPlacementConflicts() {
        return placementConflicts;
    }

    public static void makePlacementLiterals() {
        for (Placement p : placements) {
            placementLiterals.put(p, getModel().newBoolVar(p.toString()));
        }
        for (Placement p : placements) {
            for (Placement q : placements) {
                if (p.equals(q)) {
                    continue;
                }
                if (p.getRoom() == null || q.getRoom() == null) {
                    continue;
                }
                if (SameRoom.compare(p.getRoom(), q.getRoom()) && Overlap.compare(p.getTime(), q.getTime())) {
                    getModel().addBoolOr(new Literal[]{
                        placementLiterals.get(p).not(),
                        placementLiterals.get(q).not()
                    });
                }
            }
        }
    }

    public static void resolvePlacementLiteralsConflict() {
        for (Placement p : placements) {
            if (placementConflicts.containsKey(p)) {
                ArrayList<Literal> conflicts = placementConflicts.get(p);
                getModel().addAtMostOne(conflicts.toArray(new Literal[0]));
            }
        }
    }
}
