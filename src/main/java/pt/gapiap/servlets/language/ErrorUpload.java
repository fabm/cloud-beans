package pt.gapiap.servlets.language;

import com.google.common.collect.ImmutableList;
import pt.gapiap.cloud.endpoints.errors.ErrorArea;
import pt.gapiap.cloud.endpoints.errors.ErrorContent;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.servlets.language.en.UploadErrorsContentEN;
import pt.gapiap.servlets.language.pt.UploadErrorsContentPT;

import java.util.List;

public class ErrorUpload extends ErrorArea {
  public ErrorUpload(ErrorManager errorManager) {
    super(errorManager);
  }

  @Override
  protected List<? extends ErrorContent> getErrorContents() {
    return ImmutableList.of(new UploadErrorsContentPT(), new UploadErrorsContentEN());
  }

  @Override
  protected int[] getClientErrorIndexes() {
    return null;
  }
}
