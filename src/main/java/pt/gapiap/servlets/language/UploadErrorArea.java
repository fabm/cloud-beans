package pt.gapiap.servlets.language;

import com.google.common.collect.ImmutableList;
import pt.gapiap.cloud.endpoints.errors.ErrorArea;
import pt.gapiap.cloud.endpoints.errors.ErrorContent;
import pt.gapiap.servlets.language.en.UploadErrorsContentEN;
import pt.gapiap.servlets.language.pt.UploadErrorsContentPT;

import java.util.List;
import java.util.Map;

public class UploadErrorArea extends ErrorArea {

  @Override
  protected List<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new UploadErrorsContentPT(), new UploadErrorsContentEN());
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
