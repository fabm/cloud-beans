package test;

import com.google.inject.Inject;
import pt.gapiap.proccess.logger.Logger;

public class Contented {
    @Inject
    TestInjected testInjected;
    @Inject
    Logger logger;

    public void init(){
        testInjected.getPrintWriter().println("foi bem injetado");
        testInjected.getPrintWriter().close();
        logger.getPrintWriter().println("log bem injectado");
    }
}
