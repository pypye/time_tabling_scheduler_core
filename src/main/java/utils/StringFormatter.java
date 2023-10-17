package utils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class StringFormatter {
    public StringFormatter() {
    }

    public static String printObject(Object object) {
        Field[] attributes = object.getClass().getDeclaredFields();
        return String.format("%s(%s)", object.getClass().getSimpleName(), Arrays.stream(attributes).map((attribute) -> {
            try {
                attribute.setAccessible(true);
                return String.format("%s=%s", attribute.getName(), attribute.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).reduce((attribute1, attribute2) -> String.format("%s, %s", attribute1, attribute2)).orElse(""));
    }

    public static Long[] convertFromString(String timeSlot) {
        Long[] result = new Long[timeSlot.length()];
        for (int i = 0; i < timeSlot.length(); i++) {
            result[i] = timeSlot.charAt(i) == '1' ? 1L : 0L;
        }
        return result;
    }
}
