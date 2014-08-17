package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.proccess.validation.ValidationMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * class to wrap methods to facilitate the comparison
 */
class ValidationMethodCheckerImpl implements ValidationMethodChecker {
    private Method method;
    private int priority;
    private Class<? extends Annotation> validationAnnotation;

    ValidationMethodCheckerImpl(Method method, ValidationMethod validationMethod) {
        this.method = method;
        this.validationAnnotation = validationMethod.value();
        this.priority = validationMethod.priority();
    }


    @Override
    public int compareTo(ValidationMethodChecker that) {
        if (this.getPriority() > that.getPriority()) {
            return -1;
        }
        if (this.getPriority() < that.getPriority()) {
            return 1;
        }
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationValidation() {
        return this.validationAnnotation;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public Map<String,?> checkValidation(ValidationContext<?> validationContext) {
        try {
            return (Map<String,?>) method.invoke(method, validationContext);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
