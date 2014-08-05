package pt.gapiap.cloud.maps;

import com.google.inject.Inject;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.ValidationMethod;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.List;

public class ApiValidator extends HashMap<DeclaredType, ApiValidation> {
    @Inject
    Logger logger;
    private DeclaredType validator;

    public DeclaredType getValidator() {
        return validator;
    }

    public void setValidator(DeclaredType validator) {
        this.validator = validator;
    }

    void init() {
        List<ExecutableElement> methods = ElementFilter.methodsIn(validator.asElement().getEnclosedElements());
        for (ExecutableElement executableElement : methods) {
            ValidationMethod validationMethod = executableElement.getAnnotation(ValidationMethod.class);
            if (validationMethod != null) {
                check(validationMethod);
            }
        }
    }

    private void check(ValidationMethod validationMethod) {
        TypeMirror typeMirror = null;
        try {
            validationMethod.value();
        } catch (MirroredTypeException e) {
            typeMirror = e.getTypeMirror();
        }
        if (!containsKey(typeMirror)) {
            ApiValidation apiValidation = new ApiValidation();
            apiValidation.priority = validationMethod.priority();
            put((DeclaredType) typeMirror, apiValidation);
        }
    }


    @Override
    public int hashCode() {
        return validator.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return validator.equals(obj);
    }

    public boolean hasAnnotation(AnnotationMirror annotationMirror) {
        String amTypeString = annotationMirror.getAnnotationType().toString();
        for (DeclaredType key : this.keySet()) {
            String keyString = key.toString();
            if (keyString.hashCode() == amTypeString.hashCode() &&
                    keyString.equals(amTypeString)) {
                return true;
            }
        }
        return false;
    }
}
