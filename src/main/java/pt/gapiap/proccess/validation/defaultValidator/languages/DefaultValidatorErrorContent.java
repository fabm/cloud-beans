package pt.gapiap.proccess.validation.defaultValidator.languages;

import pt.gapiap.cloud.endpoints.errors.ErrorContent;

public interface DefaultValidatorErrorContent extends ErrorContent {
    int NOT_NULL = 0;
    int EMAIL = 1;
    int SIZE = 2;
    int MIN = 3;
    int MAX = 4;
}
