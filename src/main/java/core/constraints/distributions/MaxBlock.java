package core.constraints.distributions;

public class MaxBlock {
    public static boolean isMaxBlock(String t) {
        return t.contains("MaxBlock");
    }

    public static int getM(String t) {
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
