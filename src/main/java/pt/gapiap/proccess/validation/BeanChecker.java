package pt.gapiap.proccess.validation;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.annotations.Embedded;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanChecker<V> {
    protected V validator;
    protected Object validationContexts;
    protected boolean validateAll;

    public V getValidator() {
        return validator;
    }

    public <T> T check(Object object) throws BeanCheckerException{
        Class<? extends Object> cl = object.getClass();
        return check(object, cl);
    }

    private boolean continueAfterRecursive() throws IllegalAccessException {
        if (validateAll) {
            return true;
        }
        Field field = (Field) validationContexts;
        return field.get(validator) == null;
    }

    private <T> T check(Object object, Class<? extends Object> cl) throws BeanCheckerException {
        Class<?> superCl = cl.getSuperclass();
        ApiMethodParameters superAmp = superCl.getAnnotation(ApiMethodParameters.class);
        try {
            ApiMethodParameters amp = cl.getAnnotation(ApiMethodParameters.class);
            if (validator == null) {
                validator = (V) amp.validator().newInstance();
            }

            if (superAmp != null) {
                T ret = check(object, superCl);
                if (!continueAfterRecursive()) {
                    return ret;
                }
            }
            List<MethodWrapper> methodsList = new ArrayList<>();
            for (Method method : amp.validator().getMethods()) {
                ValidationMethod validationMethod = method.getAnnotation(ValidationMethod.class);
                if (validationMethod != null) {
                    methodsList.add(new MethodWrapper(method, validationMethod));
                }
            }
            initValidationContexts(validator);

            Collections.sort(methodsList);
            for (Field field : cl.getDeclaredFields()) {
                Embedded embedded = field.getAnnotation(Embedded.class);
                if (embedded != null) {
                    field.setAccessible(true);
                    return check(field.get(object));
                }
                for (MethodWrapper methodWrapper : methodsList) {
                    Annotation annotation = field.getAnnotation(methodWrapper.validationMethod.value());
                    if (annotation != null) {
                        ValidationContext<?> validationContext = createValidationContext(object, field, annotation);
                        if (!methodValidation(
                                validationContext,
                                methodWrapper.method
                        )) {
                            if (!addVCAndContinue(validationContext)) {
                                throw new BeanCheckerException(validationContext);
                            }
                        }
                    }
                }
            }

        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return (T) object;
    }

    private boolean addVCAndContinue(ValidationContext<?> validationContext) throws IllegalAccessException {
        if (validateAll) {
            ((List<ValidationContext<?>>) validationContext).add(validationContext);
        } else {
            ((Field) this.validationContexts).set(validator, validationContext);
        }
        return validateAll;
    }

    private void initValidationContexts(Object validator) {
        Class<?> clV = validator.getClass();
        for (Field f : clV.getDeclaredFields()) {
            ValidationContexts validationContexts = f.getAnnotation(ValidationContexts.class);
            if (validationContexts != null) {
                f.setAccessible(true);
                if (f.getType() == ValidationContext.class) {
                    validateAll = false;
                    this.validationContexts = f;
                } else if (f.getType() == List.class) {
                    this.validationContexts = new ArrayList<>();
                    validateAll = true;
                }
                return;
            }
        }
    }

    private boolean methodValidation(
            ValidationContext<?> validationContext,
            Method method
    ) throws InvocationTargetException, IllegalAccessException {
        return (boolean) method.invoke(validator, validationContext);
    }

    private ValidationContext<?> createValidationContext(
            Object object, Field field, Annotation annotation
    ) throws NoSuchFieldException, IllegalAccessException {
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

    private static class MethodWrapper implements Comparable<MethodWrapper> {
        Method method;
        ValidationMethod validationMethod;

        private MethodWrapper(Method method, ValidationMethod validationMethod) {
            this.method = method;
            this.validationMethod = validationMethod;
        }

        @Override
        public int compareTo(MethodWrapper o) {
            if (validationMethod.priority() > o.validationMethod.priority()) {
                return -1;
            }
            if (validationMethod.priority() < o.validationMethod.priority()) {
                return 1;
            }
            return 0;
        }
    }
}
