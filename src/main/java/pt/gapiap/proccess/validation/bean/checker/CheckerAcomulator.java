package pt.gapiap.proccess.validation.bean.checker;

import java.util.HashSet;
import java.util.Set;

public class CheckerAcomulator implements Checker {
  private Set<FailedField> failedFields;

  public CheckerAcomulator() {
    failedFields = new HashSet<>();
  }

  @Override
  public void add(FailedField failedField) {
    failedFields.add(failedField);
  }

  @Override
  public Set<FailedField> getFailedFields() {
    return failedFields;
  }
}
