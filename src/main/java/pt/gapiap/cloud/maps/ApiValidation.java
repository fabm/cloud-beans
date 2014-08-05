package pt.gapiap.cloud.maps;


import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;

public class ApiValidation{
    int priority;
    DeclaredType annotationType;

    public int getPriority() {
        return priority;
    }

    public DeclaredType getAnnotationType() {
        return annotationType;
    }
}
