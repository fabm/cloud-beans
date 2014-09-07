package pt.gapiap.cloud.endpoints.errors.language.pt;

import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleFailTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContents;


public class GlobalContentsPT implements GlobalContents {
    private FailTemplate[] array;

    public GlobalContentsPT() {
        init();
    }

    private void init() {
        array = new FailTemplate[3];
        array[NOT_AUTHORIZED] = new ParamterizedFailTemplate("O utilizador não está autorizado a entrar na área {0}");
        array[NOT_AUTHENTICATED] = new SimpleFailTemplate("O utilizador não está autenticado");
        array[UNEXPECTED] = new SimpleFailTemplate("Ocorreu um erro inesperado");
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return array;
    }
}
