package pt.gapiap.proccess.validation.defaultValidator.languages.en;


import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContents;

public class DefaultValidatorErrorsContentsEN implements DefaultValidatorErrorContents {
    private FailTemplate[] failTemplates;

    public DefaultValidatorErrorsContentsEN() {
        init();
    }

    private void init() {
        failTemplates = new FailTemplate[5];
        failTemplates[NOT_NULL] = new ParamterizedFailTemplate("Field {0} is required");
        failTemplates[EMAIL] = new ParamterizedFailTemplate("Field {0} don''t have an email format");
        failTemplates[SIZE] = new ParamterizedFailTemplate("Field {0} must be lesser then {1} and greater then {2}");
        failTemplates[MAX] = new ParamterizedFailTemplate("Field {0} must be lesser then {1}");
        failTemplates[MIN] = new ParamterizedFailTemplate("Field {0} must be greater a {1}");
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return failTemplates;
    }
}
