package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.language.en.GlobalContentEN;
import pt.gapiap.cloud.endpoints.errors.language.pt.GlobalContentPT;

import java.util.Map;

public class GlobalErrorArea extends ErrorArea {

  @Override
  protected ImmutableList<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new GlobalContentPT(), new GlobalContentEN());
  }

  @Override
  protected Map<String, ?> getArgumentsMap(int index) {
    return null;
  }

  @Override
  protected int[] getClientErrorIndexes() {
    return null;
  }

}
