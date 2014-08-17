package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.ValidationMethod;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Validator {
    private String api;
    private String method;
    private Class<?>[] validationClasses;
    private List<ValidationMethodChecker> validationsList;

    Validator(Object pojo) {
        ApiMethodParameters apiMethodParameters = pojo.getClass().getAnnotation(ApiMethodParameters.class);
        if(apiMethodParameters == null){
            throw new AnnotationFormatError("Annotation "+ApiMethodParameters.class+" is not present");
        }
        this.api = apiMethodParameters.api();
        this.method = apiMethodParameters.method();
        this.validationClasses = apiMethodParameters.validators();
        init();
    }

    public String getApi() {
        return api;
    }

    public String getMethod() {
        return method;
    }

    public Class<?>[] getValidationClasses() {
        return validationClasses;
    }

    public List<ValidationMethodChecker> getValidationsList() {
        return validationsList;
    }

    private void init() {
        List<ValidationMethodChecker> methodsList = new ArrayList<>();
        this.validationsList = new ArrayList<>();
        for (Class<?> validator : validationClasses) {
            generateListValidationMethods(validator);
        }
        Collections.sort(this.validationsList);
    }

    private void generateListValidationMethods(Class<?> validator) {
        for (Method method : validator.getDeclaredMethods()) {
            ValidationMethod validationMethod = method.getAnnotation(ValidationMethod.class);
            if (validationMethod != null) {
                validationsList.add(new ValidationMethodCheckerImpl(method, validationMethod));
            }
        }
    }

}
