package pt.gapiap.proccess.wrappers;

import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.mirrors.annotationMirror.AnnotationMirrorLoader;
import pt.gapiap.proccess.mirrors.annotationMirror.UnexpectedAnnotation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.Set;

public class Validator {
    private static final String VALIDATION_METHOD_REFERENCE = "pt.gapiap.proccess.validation.ValidationMethod";
    private static Set<Validator> cache = new HashSet<>();

    private DeclaredType declaredType;

    private Validator() {

    }

    private Set<Validation> validations = new HashSet<>();

    public static Validator getValidator(DeclaredType validatorType) {
        Validator validator = new Validator();
        validator.declaredType = validatorType;
        cache.add(validator);
        validator.init();
        return validator;
    }

    @Override
    public int hashCode() {
        return declaredType.toString().length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validator validator = (Validator) o;

        if (!declaredType.equals(validator.declaredType)) return false;

        return true;
    }

    private void init() {
        for (ExecutableElement method : ElementFilter.methodsIn(declaredType.asElement().getEnclosedElements()))
            for (AnnotationMirror annotationMirror : method.getAnnotationMirrors()) {
                try {
                    Validation validation = AnnotationMirrorLoader.loader(new Validation(), annotationMirror, VALIDATION_METHOD_REFERENCE);
                    validations.add(validation);
                } catch (UnexpectedAnnotation unexpectedAnnotation) {
                    Logger.get().log(unexpectedAnnotation);
                }
            }
    }

    public Set<Validation> getValidations() {
        return validations;
    }
}
