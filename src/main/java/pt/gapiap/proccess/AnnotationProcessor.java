package pt.gapiap.proccess;

import com.google.inject.Guice;
import com.google.inject.Injector;
import pt.gapiap.proccess.inject.AProcessorGM;
import pt.gapiap.proccess.inject.EnvironmentRun;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@SupportedAnnotationTypes({
        "pt.gapiap.proccess.annotations.ApiMethodParameters"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {
    private static Injector injector;

    private static String toUtf8(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("ISO-8859-1"), "UTF-8");
    }

    private static String toLatin1(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("UTF-8"), "ISO-8859-1");
    }

    private Injector getInjector(RoundEnvironment env) {
        if (injector == null) {
            AProcessorGM aProcessorGM = new AProcessorGM();
            aProcessorGM.setProcessingEnv(processingEnv);
            injector = Guice.createInjector(aProcessorGM);
        }
        injector.getInstance(EnvironmentRun.class).setRoundEnvironment(env);
        return injector;
    }

    private void note(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, s);
    }

    private void error(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, s);
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Injector injector = getInjector(env);
        ProcessorAction processAction = injector.getInstance(ProcessorAction.class);
        try {
            return processAction.process(annotations, env);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
