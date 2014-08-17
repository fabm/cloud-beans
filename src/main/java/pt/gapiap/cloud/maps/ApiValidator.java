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
import java.util.Map;

public class ApiValidator extends HashMap<DeclaredType, ApiValidation> {
    @Inject
    Logger logger;
    private List<? extends TypeMirror> validators;

    public List<? extends TypeMirror> getValidator() {
        return validators;
    }

    public boolean hasSameTypes(List<? extends TypeMirror> typeMirrors) {
        for (TypeMirror typeMirror : typeMirrors) {
            if (!hasTypeMirror(typeMirror)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasTypeMirror(TypeMirror typeMirror) {
        for (TypeMirror thisTypeMirror : validators) {
            if (thisTypeMirror.toString().equals(typeMirror.toString())) {
                return true;
            }
        }
        return false;
    }

    public void setValidators(List<? extends TypeMirror> validators) {
        this.validators = validators;
    }

    void init() {
        for (TypeMirror typeMirror : validators) {
            initValidator(typeMirror);
        }
    }

    private void initValidator(TypeMirror typeMirror) {
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<ExecutableElement> methods = ElementFilter.methodsIn(declaredType.asElement().getEnclosedElements());
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
            apiValidation.alias = validationMethod.alias();
            put((DeclaredType) typeMirror, apiValidation);
        }
    }


    public FieldAnnotation getFieldAnnotation(AnnotationMirror annotationMirror) {
        String amTypeString = annotationMirror.getAnnotationType().toString();

        for (Map.Entry<DeclaredType, ApiValidation> entry : this.entrySet()) {
            String keyString = entry.getKey().toString();

            if (keyString.hashCode() == amTypeString.hashCode() &&
                    keyString.equals(amTypeString)) {
                FieldAnnotation fieldAnnotation = new FieldAnnotation();
                fieldAnnotation.setAnnotationMirror(annotationMirror);
                fieldAnnotation.setApiValidation(entry.getValue());
                return fieldAnnotation;
            }
        }
        return null;
    }
}
