package pt.gapiap.cloud.maps;

import javax.lang.model.element.AnnotationMirror;

public class FieldAnnotation {
    AnnotationMirror annotationMirror;
    ApiValidation apiValidation;

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public void setAnnotationMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    public ApiValidation getApiValidation() {
        return apiValidation;
    }

    public void setApiValidation(ApiValidation apiValidation) {
        this.apiValidation = apiValidation;
    }
}
