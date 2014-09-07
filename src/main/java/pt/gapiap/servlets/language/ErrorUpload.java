package pt.gapiap.servlets.language;

import pt.gapiap.errors.ErrorArea;
import pt.gapiap.errors.ErrorManager;
import pt.gapiap.servlets.language.en.UploadErrorsContentEN;
import pt.gapiap.servlets.language.pt.UploadErrorsContentPT;

public class ErrorUpload extends ErrorArea {
    public ErrorUpload(ErrorManager errorManager) {
        super(errorManager);
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    protected void init() {
        setArrayLanguage("pt",new UploadErrorsContentPT());
        setArrayLanguage("en",new UploadErrorsContentEN());
    }
}
