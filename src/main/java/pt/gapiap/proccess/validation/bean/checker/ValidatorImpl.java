package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.cloud.endpoints.errors.ErrorArea;

import java.util.List;

public class ValidatorImpl implements Validator {
  private Class<?> validationClass;
  private ErrorArea errorArea;
  private List<ValidationMethodChecker> validationList;

  public ValidatorImpl(Class<?> validationClass, ErrorArea errorArea, List<ValidationMethodChecker> validationList) {
    this.validationClass = validationClass;
    this.errorArea = errorArea;
    this.validationList = validationList;
  }

  @Override
  public Class<?> getValidatorClass() {
    return validationClass;
  }

  @Override
  public ErrorArea getErrorArea() {
    return errorArea;
  }

  @Override
  public List<ValidationMethodChecker> getValidationList() {
    return validationList;
  }

  @Override
  public String getJsonTemplate(int code, String language) {
    return errorArea.getFailTemplate(code, language).jsonTemplate();
  }

}
