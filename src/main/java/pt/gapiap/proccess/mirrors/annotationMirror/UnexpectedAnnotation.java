package pt.gapiap.proccess.mirrors.annotationMirror;

import javax.lang.model.element.AnnotationMirror;

public class UnexpectedAnnotation extends Exception {
    public UnexpectedAnnotation(AnnotationMirror annotationMirror, String expected) {
        super("Expected "+expected+" annotationMirror and load "+annotationMirror.getAnnotationType().toString());
    }
}
