package pt.gapiap.proccess.validation.defaultValidator.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorArea;
import pt.gapiap.cloud.endpoints.errors.ErrorContent;
import pt.gapiap.proccess.validation.defaultValidator.languages.en.DefaultValidatorErrorsContentEN;
import pt.gapiap.proccess.validation.defaultValidator.languages.pt.DefaultValidatorErrorsContentPT;

import java.util.List;
import java.util.Map;

public class DefaultValidatorErrorArea extends ErrorArea {

  @Override
  protected List<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new DefaultValidatorErrorsContentEN(), new DefaultValidatorErrorsContentPT());
  }

  @Override
  protected Map<String, ?> getArgumentsMap(int index) {
    final String constraint = "constraint";
    switch (index) {
      case DefaultValidatorErrorContent.MIN:
        return ImmutableMap.of(constraint, "min");
      case DefaultValidatorErrorContent.MAX:
        return ImmutableMap.of(constraint, "max");
      case DefaultValidatorErrorContent.EMAIL:
        return ImmutableMap.of(constraint, "email");
      case DefaultValidatorErrorContent.NOT_NULL:
        return ImmutableMap.of(constraint, "required");
      case DefaultValidatorErrorContent.SIZE:
        return ImmutableMap.of(constraint, "size");
    }
    return null;
  }


  /**
   * @return client error indexes
   */
  @Override
  protected int[] getClientErrorIndexes() {
    return new int[]{
        DefaultValidatorErrorContent.NOT_NULL,
        DefaultValidatorErrorContent.EMAIL,
        DefaultValidatorErrorContent.SIZE,
        DefaultValidatorErrorContent.MAX,
        DefaultValidatorErrorContent.MIN
    };
  }


}
