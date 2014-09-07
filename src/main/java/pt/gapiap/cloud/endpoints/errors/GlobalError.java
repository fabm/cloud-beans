package pt.gapiap.cloud.endpoints.errors;

import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.language.en.GlobalContentsEN;
import pt.gapiap.cloud.endpoints.errors.language.pt.GlobalContentsPT;
import pt.gapiap.errors.ErrorArea;
import pt.gapiap.errors.ErrorManager;

public class GlobalError extends ErrorArea{

    @Inject
    public GlobalError(ErrorManager errorManager) {
        super(errorManager);
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    protected void init() {
        setArrayLanguage("pt",new GlobalContentsPT());
        setArrayLanguage("en",new GlobalContentsEN());
    }
}
