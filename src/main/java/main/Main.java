package main;

import com.google.ortools.Loader;
import core.Solver;

public class Main {
    public Main() {
    }

    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
//        new DataLoader("src/main/resources/data/lums-sum17.xml");
        new Solver();
    }

}