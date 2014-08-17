package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.proccess.annotations.Embedded;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * Class to validate beans, mainly it's an engine witch uses a validator bean
 * like {@link pt.gapiap.proccess.validation.defaultValidator.DefaultValidator} to run the methods annotated with
 * {@link pt.gapiap.proccess.validation.ValidationMethod} each method will run in is the priority order
 * and will be injected the context of the field
 */
public class BeanChecker {
    protected Validator validator;
    protected Checker checker;

    public BeanChecker(boolean checkAllErrors) {
        if(checkAllErrors){
            checker = new CheckerAcomulator();
        }else{
            checker = new SimpleChecker();
        }
    }

    /**
     * @return the current validator
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Checks the validation of a bean
     *
     * @param object
     * @param classes
     * @param <T>     object type
     * @return
     * @throws BeanCheckerException
     */

    @SuppressWarnings("unchecked")
    public <T> T check(T object, Class<? extends T>... classes) {
        validator = new Validator(object);

        for (Class<? extends T> clazz : classes) {
            check(object, clazz);
        }
        return object;
    }

    private void check(Object object, Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                Embedded embedded = field.getAnnotation(Embedded.class);
                if (embedded != null) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    check(value, clazz);
                }
                validateMethods(object, field);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateField(Object object, Field field, ValidationMethodChecker validationMethodChecker) {

        Annotation annotation = field.getAnnotation(validationMethodChecker.annotationValidation());
        if (annotation != null) {
            try {
                if (validationMethodChecker.annotationValidation().hashCode() != annotation.annotationType().hashCode()) {
                    return;
                }
                if (!validationMethodChecker.annotationValidation().equals(annotation.annotationType())) {
                    return;
                }
                ValidationContext<?> validationContext = createValidationContext(object, field, annotation);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void validateMethods(Object object, Field field) {
        for (ValidationMethodChecker validationMethodChecker : validator.getValidationsList()) {
            validateField(object, field, validationMethodChecker);
        }
    }

    private void validates(ValidationContext<?> validationContext, ValidationMethodChecker validationMethodChecker) {
        Map<String, ?> checkedResult = validationMethodChecker.checkValidation(validationContext);
        if (checkedResult!=null) {
            //TODO fix
            //checker.check(checkedResult);
        }
    }



    private boolean methodValidation(
            ValidationContext<?> validationContext,
            Method method
    ) throws InvocationTargetException, IllegalAccessException {
        return (boolean) method.invoke(validator, validationContext);
    }

    private ValidationContext<?> createValidationContext(
            Object object, Field field, Annotation annotation) throws NoSuchFieldException, IllegalAccessException {
        ValidationContext<?> validationContext = new ValidationContext<>();
        Field fieldVC = ValidationContext.class.getDeclaredField("field");
        Field objectVC = ValidationContext.class.getDeclaredField("object");
        Field annotationVC = ValidationContext.class.getDeclaredField("annotation");
        field.setAccessible(true);

        for (Field f : new Field[]{fieldVC, objectVC, annotationVC}) {
            f.setAccessible(true);
        }

        fieldVC.set(validationContext, field);
        objectVC.set(validationContext, object);
        annotationVC.set(validationContext, annotation);
        return validationContext;
    }
}
