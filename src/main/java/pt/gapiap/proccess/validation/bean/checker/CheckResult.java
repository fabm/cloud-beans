package pt.gapiap.proccess.validation.bean.checker;

import java.lang.annotation.Annotation;
import java.util.Map;

public class CheckResult {
    private String fieldName;
    private Annotation annotation;

    public CheckResult(String fieldName) {
        this.fieldName = fieldName;
    }

    String getFieldName() {
        return fieldName;
    }

}
