//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
            } catch (IllegalAccessException var3) {
                throw new RuntimeException(var3);
            }
        }).reduce((attribute1, attribute2) -> {
            return String.format("%s, %s", attribute1, attribute2);
        }).orElse(""));
    }
}
