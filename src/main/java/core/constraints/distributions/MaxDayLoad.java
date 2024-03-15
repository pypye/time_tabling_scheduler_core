package core.constraints.distributions;

public class MaxDayLoad {
    public static boolean isMaxDayLoad(String t) {
        return t.contains("MaxDayLoad");
    }

    public static int getS(String t) {
        int pos0 = t.indexOf("(");
        int pos1 = t.indexOf(")");
        return Integer.parseInt(t.substring(pos0 + 1, pos1));
    }
}
