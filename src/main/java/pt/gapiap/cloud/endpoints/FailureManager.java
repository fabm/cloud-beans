package pt.gapiap.cloud.endpoints;

import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;

public class FailureManager {
  private String language;
  private ErrorManager errorManager;

  public FailureManager(String language, ErrorManager errorManager) {
    this.language = language;
    this.errorManager = errorManager;
  }

  public String getLanguage() {
    return language;
  }

  public ErrorManager getErrorManager() {
    return errorManager;
  }

  public CEError create(int index, Object... vars) {
    return getErrorManager().create(index, language, vars);
  }

}
