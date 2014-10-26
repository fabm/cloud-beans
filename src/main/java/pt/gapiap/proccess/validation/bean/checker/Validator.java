package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.cloud.endpoints.errors.ErrorArea;

import java.util.List;

interface Validator {
    Class<?> getValidatorClass();

    List<ValidationMethodChecker> getValidationList();

    String getJsonTemplate(int code);
}
