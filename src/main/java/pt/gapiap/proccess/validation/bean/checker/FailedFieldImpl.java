package pt.gapiap.proccess.validation.bean.checker;

class FailedFieldImpl implements FailedField {
  private String field;
  private String message;

  @Override
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
