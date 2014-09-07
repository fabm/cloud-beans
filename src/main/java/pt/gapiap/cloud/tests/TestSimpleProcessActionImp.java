package pt.gapiap.cloud.tests;

import com.google.inject.Inject;
import pt.gapiap.proccess.ProcessorAction;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

public class TestSimpleProcessActionImp implements ProcessorAction {
    @Inject
    private ProcessingEnvironment processingEnvironment;
    @Inject
    private Logger logger;


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //return callProcess(annotations, roundEnv);
        return false;
    }

    private boolean callProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement typeElement : annotations) {
            logger.printLn(typeElement.getQualifiedName().toString());
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ApiMethodParameters.class);
            for (Element element : elements) {
                printElement(element);
            }
        }
        logger.close();
        return false;
    }

    private void printElement(Element element) {
        ApiMethodParameters apiMethodParameters = element.getAnnotation(ApiMethodParameters.class);
        logger.printLn(apiMethodParameters.api().toString());
        try {
            Class<?>[] validators = apiMethodParameters.validators();
        } catch (MirroredTypesException e) {
            for(TypeMirror typeMirror:e.getTypeMirrors()){
                logger.printLn("typemirror:"+typeMirror.toString());
            }
        }
        logger.printLn("Element:"+element.getSimpleName().toString());
    }
}
