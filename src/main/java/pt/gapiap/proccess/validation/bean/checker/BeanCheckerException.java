package pt.gapiap.proccess.validation.bean.checker;


import java.util.Map;

public class BeanCheckerException extends RuntimeException {
    ValidationContext<?> validationContext;
    Map<String,?> failure;

    public ValidationContext<?> getValidationContext() {
        return validationContext;
    }

    public Map<String,?> getFailure(){
        return failure;
    }
}
