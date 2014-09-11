package pt.gapiap.proccess.validation.defaultValidator.languages.pt;


import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContent;

public class DefaultValidatorErrorsContentPT implements DefaultValidatorErrorContent {
    private ErrorTemplate[] errorTemplates;


    public DefaultValidatorErrorsContentPT() {
        init();
    }

    private void init() {
        errorTemplates = new ErrorTemplate[5];
        errorTemplates[NOT_NULL] = new ParamterizedErrorTemplate("O campo {0} é obrigatório");
        errorTemplates[EMAIL] = new ParamterizedErrorTemplate("O campo {0} não está de acordo com o formato de um email");
        errorTemplates[SIZE] = new ParamterizedErrorTemplate("O campo {0} deve ser inferior a {1} e superior a {2}");
        errorTemplates[MAX] = new ParamterizedErrorTemplate("O campo {0} deve ser inferior a {1}");
        errorTemplates[MIN] = new ParamterizedErrorTemplate("O campo {0} deve ser superior a {1}");
    }

  @Override
  public String getLanguage() {
    return "pt";
  }

  @Override
    public ErrorTemplate[] getErrorTemplates() {
        return errorTemplates;
    }

}
