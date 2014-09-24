package pt.gapiap.proccess.validation.bean.checker;

import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;

import java.util.List;

public class ValidatorImpl implements Validator {
  private Class<?> validationClass;
  @Inject
  private ErrorManager errorManager;
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
  public String getJsonTemplate(int code, String language) {
    return errorManager.getFailTemplate(code, language).jsonTemplate();
  }

}
