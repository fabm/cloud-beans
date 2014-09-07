package pt.gapiap.cloud.endpoints.errors.language.en;

import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleFailTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContents;


public class GlobalContentsEN implements GlobalContents {

    private FailTemplate[] array;

    public GlobalContentsEN() {
        init();
    }

    private void init() {
        array = new FailTemplate[3];
        array[NOT_AUTHORIZED] = new ParamterizedFailTemplate("You don't have previleges to enter in {0} area");
        array[NOT_AUTHENTICATED] = new SimpleFailTemplate("Please, authenticate with your account to access this area");
        array[UNEXPECTED] = new SimpleFailTemplate("Unexpected error occurred");
    }

    @Override
    public FailTemplate[] getFailTemplates() {
        return array;
    }
}
