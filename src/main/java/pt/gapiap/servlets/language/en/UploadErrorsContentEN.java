package pt.gapiap.servlets.language.en;

import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleFailTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

public class UploadErrorsContentEN implements UploadErrorsContent {
    private FailTemplate[] failTemplates;

    public UploadErrorsContentEN() {
        init();
    }

    private void init() {
        failTemplates = new FailTemplate[4];
        failTemplates[NO_ACTION_PARAMETER] = new SimpleFailTemplate("There is no parameter for the action",true);
        failTemplates[NO_UPLOAD_ACTION_REGISTERED] = new ParamterizedFailTemplate("There is no upload registered for the action '{0}'");
        failTemplates[KEY_NOT_NULL_STRING] = new ParamterizedFailTemplate("Key of method {0} must be a not null String");
        failTemplates[KEY_ALREADY_EXISTS] = new ParamterizedFailTemplate("The key {0} already exists");
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return failTemplates;
    }
}
