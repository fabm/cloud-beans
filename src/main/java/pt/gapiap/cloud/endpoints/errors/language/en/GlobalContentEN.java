package pt.gapiap.cloud.endpoints.errors.language.en;

import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;


public class GlobalContentEN implements GlobalContent {

    private ErrorTemplate[] array;

    public GlobalContentEN() {
        init();
    }

    private void init() {
        array = new ErrorTemplate[3];
        array[NOT_AUTHORIZED] = new ParamterizedErrorTemplate("You don't have previleges to enter in {0} area");
        array[NOT_AUTHENTICATED] = new SimpleErrorTemplate("Please, authenticate with your account to access this area");
        array[UNEXPECTED] = new SimpleErrorTemplate("Unexpected error occurred");
    }

  @Override
  public String getLanguage() {
    return "en";
  }

  @Override
    public ErrorTemplate[] getErrorTemplates() {
        return array;
    }
}
