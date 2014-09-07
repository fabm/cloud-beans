package pt.gapiap.servlets.language.pt;


import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleFailTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

public class UploadErrorsContentPT implements UploadErrorsContent{

    private FailTemplate[] failTemplates;

    public UploadErrorsContentPT() {
        init();
    }

    private void init() {
        failTemplates = new FailTemplate[4];
        failTemplates[NO_ACTION_PARAMETER] = new SimpleFailTemplate("Não existem parametros para a ação");
        failTemplates[NO_UPLOAD_ACTION_REGISTERED] = new ParamterizedFailTemplate("Não foi registado upload para a ação '{0}'");
        failTemplates[KEY_NOT_NULL_STRING] = new ParamterizedFailTemplate("A chave do método {0} tem que ser uma string não vazia");
        failTemplates[KEY_ALREADY_EXISTS] = new ParamterizedFailTemplate("A chave '{0}' já existe");
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return failTemplates;
    }
}
