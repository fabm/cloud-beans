package pt.gapiap.proccess.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;

public class AProcessorGM extends AbstractModule {

    private ProcessingEnvironment processingEnv;
    private RoundEnvironment roundEnvironment;

    public void setProcessingEnv(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
    }

    private PrintWriter getCompileLogWriter() {
        try {
            return new PrintWriter(processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "compile.log").openWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    private RoundEnvironment getRoundEnvironment() {
        return roundEnvironment;
    }

    public void setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    @Override
    protected void configure() {
        bind(Logger.class).toInstance(new Logger(getCompileLogWriter()));
    }

}
