package pt.gapiap.proccess.validation.defaultValidator.languages.pt;


import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContents;

public class DefaultValidatorErrorsContentsPT implements DefaultValidatorErrorContents {
    private FailTemplate[] failTemplates;


    public DefaultValidatorErrorsContentsPT() {
        init();
    }

    private void init() {
        failTemplates = new FailTemplate[5];
        failTemplates[NOT_NULL] = new ParamterizedFailTemplate("O campo {0} é obrigatório", true);
        failTemplates[EMAIL] = new ParamterizedFailTemplate("O campo {0} não está de acordo com o formato de um email", true);
        failTemplates[SIZE] = new ParamterizedFailTemplate("O campo {0} deve ser inferior a {1} e superior a {2}", true);
        failTemplates[MAX] = new ParamterizedFailTemplate("O campo {0} deve ser inferior a {1}", true);
        failTemplates[MIN] = new ParamterizedFailTemplate("O campo {0} deve ser superior a {1}", true);
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return failTemplates;
    }

}
