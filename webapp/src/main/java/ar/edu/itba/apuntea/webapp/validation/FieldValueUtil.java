package ar.edu.itba.apuntea.webapp.validation;

import java.lang.reflect.Field;

public class FieldValueUtil {

    public static Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}