package pt.gapiap.proccess.validation.defaultValidator.languages;

import pt.gapiap.cloud.endpoints.errors.ErrorContent;

public interface DefaultValidatorErrorContent extends ErrorContent {
  int NOT_NULL = 1001;
  int EMAIL = 1002;
  int SIZE = 1003;
  int MIN = 1004;
  int MAX = 1005;
  int DATE_FUTURE = 1006;
  int DATE_PAST = 1007;
  int DATE_BETWEEN = 1008;
}
