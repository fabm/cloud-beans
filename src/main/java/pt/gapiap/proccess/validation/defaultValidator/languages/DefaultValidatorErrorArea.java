package pt.gapiap.proccess.validation.defaultValidator.languages;

import pt.gapiap.errors.ErrorArea;
import pt.gapiap.errors.ErrorManager;
import pt.gapiap.proccess.validation.defaultValidator.languages.en.DefaultValidatorErrorsContentsEN;
import pt.gapiap.proccess.validation.defaultValidator.languages.pt.DefaultValidatorErrorsContentsPT;

public class DefaultValidatorErrorArea extends ErrorArea {
    public DefaultValidatorErrorArea(ErrorManager errorManager) {
        super(errorManager);
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    protected void init() {
        setArrayLanguage("en", new DefaultValidatorErrorsContentsEN());
        setArrayLanguage("pt", new DefaultValidatorErrorsContentsPT());
    }
}
