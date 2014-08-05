package pt.gapiap.proccess.wrappers;

import javax.lang.model.type.DeclaredType;

public class ApiMethodPMW extends AnnotationWrapper {
    private String method;
    private String api;
    private DeclaredType validator;

    public String getMethod() {
        return method;
    }

    public String getApi() {
        return api;
    }

    public DeclaredType getValidator() {
        return validator;
    }

    @Override
    protected boolean filterAnnotationValue(String key, Object value) {
        switch (key) {
            case "method":
                method = value.toString();
                return true;
            case "api":
                api = value.toString();
                return true;
            case "validator":
                validator = (DeclaredType) value;
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Iterable<ApiField> getFields() {
        return new ApiFieldsIterable(this);
    }
}
