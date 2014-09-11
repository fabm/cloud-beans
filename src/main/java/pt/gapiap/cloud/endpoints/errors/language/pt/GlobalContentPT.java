package pt.gapiap.cloud.endpoints.errors.language.pt;

import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;


public class GlobalContentPT implements GlobalContent {
    private ErrorTemplate[] array;

    public GlobalContentPT() {
        init();
    }

    private void init() {
        array = new ErrorTemplate[3];
        array[NOT_AUTHORIZED] = new ParamterizedErrorTemplate("O utilizador não está autorizado a entrar na área {0}");
        array[NOT_AUTHENTICATED] = new SimpleErrorTemplate("O utilizador não está autenticado");
        array[UNEXPECTED] = new SimpleErrorTemplate("Ocorreu um erro inesperado");
    }

  @Override
  public String getLanguage() {
    return "pt";
  }

  @Override
    public ErrorTemplate[] getErrorTemplates() {
        return array;
    }
}
