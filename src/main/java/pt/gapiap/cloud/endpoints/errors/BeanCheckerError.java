package pt.gapiap.cloud.endpoints.errors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class BeanCheckerError extends CEError{
    Set<FieldFailed> fieldsFailed;

    public Set<FieldFailed> getFieldsFailed() {
        return fieldsFailed;
    }

    public void setFieldsFailed(Set<FieldFailed> fieldsFailed) {
        this.fieldsFailed = fieldsFailed;
    }
}
