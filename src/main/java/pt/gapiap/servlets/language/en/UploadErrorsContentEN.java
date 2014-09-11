package pt.gapiap.servlets.language.en;

import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

public class UploadErrorsContentEN implements UploadErrorsContent {
    private ErrorTemplate[] errorTemplates;

    public UploadErrorsContentEN() {
        init();
    }

    private void init() {
        errorTemplates = new ErrorTemplate[4];
        errorTemplates[NO_ACTION_PARAMETER] = new SimpleErrorTemplate("There is no parameter for the action");
        errorTemplates[NO_UPLOAD_ACTION_REGISTERED] = new ParamterizedErrorTemplate("There is no upload registered for the action '{0}'");
        errorTemplates[KEY_NOT_NULL_STRING] = new ParamterizedErrorTemplate("Key of method {0} must be a not null String");
        errorTemplates[KEY_ALREADY_EXISTS] = new ParamterizedErrorTemplate("The key {0} already exists");
    }

  @Override
  public String getLanguage() {
    return "en";
  }

  @Override
    public ErrorTemplate[] getErrorTemplates() {
        return errorTemplates;
    }
}
