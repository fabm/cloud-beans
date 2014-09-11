package pt.gapiap.proccess.validation.defaultValidator.languages.en;


import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContent;

public class DefaultValidatorErrorsContentEN implements DefaultValidatorErrorContent {
    private ErrorTemplate[] errorTemplates;

    public DefaultValidatorErrorsContentEN() {
        init();
    }

    private void init() {
        errorTemplates = new ErrorTemplate[5];
        errorTemplates[NOT_NULL] = new ParamterizedErrorTemplate("Field {0} is required");
        errorTemplates[EMAIL] = new ParamterizedErrorTemplate("Field {0} don''t have an email format");
        errorTemplates[SIZE] = new ParamterizedErrorTemplate("Field {0} must be lesser then {1} and greater then {2}");
        errorTemplates[MAX] = new ParamterizedErrorTemplate("Field {0} must be lesser then {1}");
        errorTemplates[MIN] = new ParamterizedErrorTemplate("Field {0} must be greater a {1}");
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
