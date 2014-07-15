package pt.gapiap.proccess.wrappers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationWrapperFactory {

    private static final String API_METHOD_PARAMETERS_CLASS = "pt.gapiap.proccess.annotations.ApiMethodParameters";
    private static final String VALIDATION_METHOD_CLASS = "pt.gapiap.proccess.validation.ValidationMethod";

    private static final String[] classes = new String[]{
            API_METHOD_PARAMETERS_CLASS, VALIDATION_METHOD_CLASS
    };
    private List<String> classList = new ArrayList<>(Arrays.asList(classes));

    protected boolean foundAnnotation(AnnotationMirror annotationMirror) {
        for (String annotationStringClass : classList) {
            if (annotationMirror.getAnnotationType().toString().equals(annotationStringClass)) {
                classList.remove(annotationStringClass);
                return true;
            }
        }
        return false;
    }

    public static List<AnnotationWrapper> createAnnotations(Element element) throws InvalidElementException {
        AnnotationWrapperFactory annotationWrapperFactory = new AnnotationWrapperFactory();
        return annotationWrapperFactory.create(element);
    }

    public List<AnnotationWrapper> create(Element element) throws InvalidElementException {
        List<AnnotationWrapper> listOut = new ArrayList<>(classes.length);
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (foundAnnotation(annotationMirror)) {
                AnnotationWrapper annotationWrapper;
                switch (annotationMirror.getAnnotationType().toString()) {
                    case API_METHOD_PARAMETERS_CLASS:
                        annotationWrapper = new ApiMethodPMW();
                        break;
                    case VALIDATION_METHOD_CLASS:
                        annotationWrapper = new ValidationMethodWrapper();
                        break;
                    default:
                        throw new InvalidElementException();
                }
                annotationWrapper.annotationMirror = annotationMirror;
                annotationWrapper.element = element;
                annotationWrapper.init();
                listOut.add(annotationWrapper);
            }
        }
        return listOut;
    }

}
