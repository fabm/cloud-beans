package pt.gapiap.proccess;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({
    "pt.gapiap.proccess.annotations.ApiMethodParameters"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DumpAnnotationProcessor extends AnnotationProcessor{
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"teste");
    return super.process(annotations, env);
  }
}
