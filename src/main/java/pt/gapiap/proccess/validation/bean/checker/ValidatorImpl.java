package pt.gapiap.proccess.validation.bean.checker;

import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.FailureManager;

import java.util.List;

public class ValidatorImpl implements Validator {
  private Class<?> validationClass;
  @Inject
  private FailureManager failureManager;
  private List<ValidationMethodChecker> validationList;

  public ValidatorImpl(Class<?> validationClass, List<ValidationMethodChecker> validationList) {
    this.validationClass = validationClass;
    this.validationList = validationList;
  }

  @Override
  public Class<?> getValidatorClass() {
    return validationClass;
  }

  @Override
  public List<ValidationMethodChecker> getValidationList() {
    return validationList;
  }

  @Override
  public String getJsonTemplate(int code) {
    return failureManager.getFailTemplate(code).jsonTemplate();
  }

}
