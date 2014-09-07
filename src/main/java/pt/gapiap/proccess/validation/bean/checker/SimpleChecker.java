package pt.gapiap.proccess.validation.bean.checker;

import com.google.common.collect.Sets;

import java.util.Set;

public class SimpleChecker implements Checker {
  private FailedField failedField;

  @Override
  public void add(FailedField failedField) {
    this.failedField = failedField;
  }

  @Override
  public Set<FailedField> getFailedFields() {
    return Sets.newHashSet(failedField);
  }
}
