package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.language.en.GlobalContentEN;
import pt.gapiap.cloud.endpoints.errors.language.pt.GlobalContentPT;

public class GlobalError extends ErrorArea {

  @Inject
  public GlobalError(ErrorManager errorManager) {
    super(errorManager);
  }

  @Override
  protected ImmutableList<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new GlobalContentPT(), new GlobalContentEN());
  }

  @Override
  protected int[] getClientErrorIndexes() {
    return null;
  }


}
