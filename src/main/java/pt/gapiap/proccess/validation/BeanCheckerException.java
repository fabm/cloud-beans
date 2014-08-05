package pt.gapiap.proccess.validation;

public class BeanCheckerException extends Exception{
    private ValidationContext<?> validationContext;

    public BeanCheckerException(ValidationContext<?> validationContext) {
        this.validationContext = validationContext;
    }

    public ValidationContext<?> getValidationContext() {
        return validationContext;
    }
}
