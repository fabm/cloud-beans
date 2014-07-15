package pt.gapiap.proccess.validation;

import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Required;

public class DefaultValidator {

    private boolean nullable = false;

    @ValidationContexts
    private ValidationContext<?> validationContext;

    @ValidationMethod(value = Required.class, priority = 1)
    public boolean valRequired(ValidationContext<Required> context) {
        nullable = true;
        if (context.isNull() ||
                context.getType() == String.class &&
                        context.getValue().toString().isEmpty())
            return false;
        return true;
    }

    @ValidationMethod(Email.class)
    public boolean valEmail(ValidationContext<Email> context) {
        if (!nullable && context.isNull())
            return false;
        return EmailChecker.check(context.getValue());
    }


    public ValidationContext<?> getValidationContext() {
        return validationContext;
    }
}
