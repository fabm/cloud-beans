package pt.gapiap.proccess;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public interface ProcessorAction {
    boolean process(Set<? extends TypeElement> annotations,
                    RoundEnvironment roundEnv);
}
