package pt.gapiap.proccess.validation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ValidationContext<T extends Annotation> {
    private Field field;
    private Object object;
    private T annotation;

    public boolean isNull() {
        return getValue() == null;
    }

    public T getAnnotation() {
        return annotation;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getName() {
        return field.getName();
    }

    public Object getValue() {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getContainer() {
        return object;
    }
}
