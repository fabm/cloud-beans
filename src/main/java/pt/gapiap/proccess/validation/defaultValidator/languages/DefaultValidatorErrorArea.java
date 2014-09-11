package pt.gapiap.proccess.validation.defaultValidator.languages;

import com.google.common.collect.ImmutableList;
import pt.gapiap.cloud.endpoints.errors.ErrorArea;
import pt.gapiap.cloud.endpoints.errors.ErrorContent;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.proccess.validation.defaultValidator.languages.pt.DefaultValidatorErrorsContentPT;

import java.util.List;

public class DefaultValidatorErrorArea extends ErrorArea {
  public DefaultValidatorErrorArea(ErrorManager errorManager) {
    super(errorManager);
  }

  @Override
  protected List<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new DefaultValidatorErrorsContentPT(), new DefaultValidatorErrorsContentPT());
  }

  @Override
  protected int[] getClientErrorIndexes() {
    return null;
  }


}
