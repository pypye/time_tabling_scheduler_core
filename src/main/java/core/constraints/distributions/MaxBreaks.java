package core.constraints.distributions;

public class MaxBreaks {
    public static boolean isMaxBreaks(String t) {
        return t.contains("MaxBreaks");
    }

    public static int getR(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }

    public static int getS(String t) {
        int pos1 = t.indexOf(",");
        int pos2 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos1 + 1, pos2));
    }
}
