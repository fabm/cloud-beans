package pt.gapiap.proccess.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import pt.gapiap.cloud.tests.TestPrintRefs;
import pt.gapiap.proccess.ApiProcessor;
import pt.gapiap.proccess.ProcessorAction;
import pt.gapiap.cloud.tests.TestSimpleProcessActionImp;
import pt.gapiap.proccess.json.writer.AnProcWriters;
import pt.gapiap.proccess.json.writer.AnProcWritersImpl;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;

public class AProcessorGM extends AbstractModule implements EnvironmentRun {

    private ProcessingEnvironment processingEnv;
    private RoundEnvironment roundEnvironment;

    public void setProcessingEnv(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
    }

    private PrintWriter getCompileLogWriter() {
        try {
            return new PrintWriter(processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "compile.log").openWriter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    private RoundEnvironment getRoundEnvironment() {
        return roundEnvironment;
    }

    @Override
    public void setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    @Override
    protected void configure() {
        bind(ProcessingEnvironment.class).toInstance(processingEnv);

        Logger logger = new Logger(getCompileLogWriter());

        bind(AnProcWriters.class).toInstance(new AnProcWritersImpl(logger,processingEnv));
        bind(Logger.class).toInstance(logger);
        //Alternate to tests implementations
        bind(ProcessorAction.class).to(TestSimpleProcessActionImp.class);
        bind(EnvironmentRun.class).toInstance(this);
    }



}
