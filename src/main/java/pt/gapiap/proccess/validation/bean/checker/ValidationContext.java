package pt.gapiap.proccess.validation.bean.checker;


import pt.gapiap.proccess.validation.bean.checker.proxy.mark.AnnotationProxyMark;
import pt.gapiap.proccess.validation.bean.checker.proxy.mark.AnnotationProxyMarkWOriginal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ValidationContext<T extends Annotation> {
    AnnotationProxyMark<T> annotationProxyMark;
    private Field field;
    private Object object;
    private T annotation;
    private Map<String, Object> mapReturnedValues;

    public Map<String, Object> getMapReturnedValues() {
        if (mapReturnedValues == null) {
            mapReturnedValues.put("field",getName());
            mapReturnedValues = new HashMap<>();
        }
        return mapReturnedValues;
    }

    public void setMapReturnedValues(Map<String, Object> mapReturnedValues) {
        this.mapReturnedValues = mapReturnedValues;
    }

    public AnnotationProxyMark<T> getAnnotationProxy() {
        if (annotationProxyMark == null) {
            annotationProxyMark = new AnnotationProxyMarkWOriginal<>(annotation, getMapReturnedValues());
        }
        return annotationProxyMark;
    }

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

    public boolean isString() {
        return getType() == String.class;
    }

    public boolean isCollection() {
        return getType().isAssignableFrom(Collection.class);
    }

    public boolean isArryay() {
        return getType().isArray();
    }

    public boolean isAPermittedNull(boolean permitted) {
        return permitted && isNull();
    }

    public boolean isEmptyString() {
        return getType() == String.class && getValue().toString().isEmpty();
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
