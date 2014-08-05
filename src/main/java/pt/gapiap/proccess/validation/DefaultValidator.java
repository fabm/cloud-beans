package pt.gapiap.proccess.validation;

import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Required;
import pt.gapiap.proccess.validation.annotations.Size;

public class DefaultValidator {

    private boolean nullable = false;

    @ValidationContexts
    private ValidationContext<?> validationContext;

    @ValidationMethod(value = Required.class, priority = 1)
    public boolean valRequired(ValidationContext<Required> context) {
        nullable = true;
        if (context.isNull() ||
                context.getType() == String.class &&
                        context.getValue().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    @ValidationMethod(Email.class)
    public boolean valEmail(ValidationContext<Email> context) {
        if (!nullable && context.isNull()) {
            return false;
        }
        return EmailChecker.check(context.getValue());
    }

    @ValidationMethod(Size.class)
    public boolean valSize(ValidationContext<Size> context) {
        if (!nullable && context.isNull()) {
            return false;
        }
        int value = (Integer) context.getValue();
        if (value > context.getAnnotation().max()) {
            return false;
        }
        if (value < context.getAnnotation().min()) {
            return false;
        }
        return true;
    }


    public ValidationContext<?> getValidationContext() {
        return validationContext;
    }
}
