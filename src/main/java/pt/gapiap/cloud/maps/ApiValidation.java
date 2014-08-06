package pt.gapiap.cloud.maps;


import javax.lang.model.type.DeclaredType;

public class ApiValidation {
    int priority;
    DeclaredType annotationType;
    String alias;

    public String getAlias() {
        return alias;
    }

    public int getPriority() {
        return priority;
    }

    public DeclaredType getAnnotationType() {
        return annotationType;
    }
}
