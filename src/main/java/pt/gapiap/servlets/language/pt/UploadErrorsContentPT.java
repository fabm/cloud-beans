package pt.gapiap.servlets.language.pt;


import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

public class UploadErrorsContentPT implements UploadErrorsContent{

    private ErrorTemplate[] errorTemplates;

    public UploadErrorsContentPT() {
        init();
    }

    private void init() {
        errorTemplates = new ErrorTemplate[4];
        errorTemplates[NO_ACTION_PARAMETER] = new SimpleErrorTemplate("Não existem parametros para a ação");
        errorTemplates[NO_UPLOAD_ACTION_REGISTERED] = new ParamterizedErrorTemplate("Não foi registado upload para a ação '{0}'");
        errorTemplates[KEY_NOT_NULL_STRING] = new ParamterizedErrorTemplate("A chave do método {0} tem que ser uma string não vazia");
        errorTemplates[KEY_ALREADY_EXISTS] = new ParamterizedErrorTemplate("A chave '{0}' já existe");
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
