package utils;

public class TimeConvert {
    public static String encode(String start, String length, String week, String day) {
        return start + "_" + length + "_" + week + "_" + day;
    }
}
