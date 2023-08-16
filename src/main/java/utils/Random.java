package utils;

public class Random {

    public static String getRandomHexString(int numChars) {
        java.util.Random r = new java.util.Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.substring(0, numChars);
    }

    public static String getRandomHexString() {
        return getRandomHexString(16);
    }
}
