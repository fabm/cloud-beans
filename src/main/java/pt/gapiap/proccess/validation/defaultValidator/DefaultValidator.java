package pt.gapiap.proccess.validation.defaultValidator;

import pt.gapiap.proccess.validation.EmailChecker;
import pt.gapiap.proccess.validation.ValidationMethod;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.bean.checker.ValidationContext;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.Collection;

public class DefaultValidator {

    private boolean nullable = true;

    @ValidationMethod(value = NotNull.class, priority = 1, alias = "required", error = 1)
    public boolean valRequired(ValidationContext<NotNull> context) {
        nullable = false;
        return context.isNull() || context.isEmptyString();
    }

    @ValidationMethod(value = Email.class, alias = "email", error = 2)
    public boolean valEmail(ValidationContext<Email> context) {
        return !(context.isAPermittedNull(nullable) || EmailChecker.check(context));
    }

    private int valueForSize(ValidationContext<?> context) {
        if (context.isCollection()) {
            Collection<?> colletion = (Collection<?>) context.getValue();
            return colletion.size();
        } else if (context.isArryay()) {
            return Array.getLength(context.getValue());
        } else if (context.isString()) {
            return context.getValue().toString().length();
        } else {
            return (Integer) context.getValue();
        }
    }

    @ValidationMethod(value = Size.class, alias = "size", error = 3)
    public boolean valSize(ValidationContext<Size> context) {
        if (context.isAPermittedNull(nullable)) {
            return true;
        }
        int value = valueForSize(context);

        Size sizeProxy = context.getAnnotationProxy().getProxy();

        if (value > sizeProxy.min() &&
                value < sizeProxy.max()) {
            return true;
        }
        return false;
    }

    @ValidationMethod(value = Min.class, alias = "size", error = 4)
    public boolean valMin(ValidationContext<Min> context) {
        if (context.isAPermittedNull(nullable)) {
            return true;
        }
        int value = valueForSize(context);

        if (value > context.getAnnotationProxy().getProxy().value()) {
            return true;
        }
        return false;
    }

    @ValidationMethod(value = Max.class, alias = "size", error = 5)
    public boolean valMax(ValidationContext<Max> context) {
        if (context.isAPermittedNull(nullable)) {
            return true;
        }
        int value = valueForSize(context);

        Max maxProxy = context.getAnnotationProxy().getProxy();

        return value < maxProxy.value();
    }
}
