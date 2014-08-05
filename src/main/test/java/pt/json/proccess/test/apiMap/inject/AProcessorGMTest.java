package pt.json.proccess.test.apiMap.inject;

import com.google.inject.AbstractModule;
import pt.gapiap.convert.RoundEnvironmentCAB;
import pt.gapiap.proccess.logger.Logger;
import pt.json.proccess.test.examples.AnnotatedObject;
import pt.json.proccess.test.examples.AnnotatedObjectYet;
import pt.json.proccess.test.examples.OtherAnnotatedObject;

import javax.annotation.processing.RoundEnvironment;
import java.io.PrintWriter;
import java.util.Arrays;

public class AProcessorGMTest extends AbstractModule {


    @Override
    protected void configure() {
        bind(Logger.class).toInstance(new Logger(new PrintWriter(System.out)));
        bind(RoundEnvironment.class).toInstance(new RoundEnvironmentCAB(
                Arrays.asList(
                        AnnotatedObjectYet.class,
                        AnnotatedObject.class,
                        OtherAnnotatedObject.class
                )
        ));
    }


}
