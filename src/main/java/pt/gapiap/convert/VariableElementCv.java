package pt.gapiap.convert;


import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class VariableElementCv implements VariableElement {
    private Field field;

    public VariableElementCv(Field field) {
        this.field = field;
    }

    @Override
    public Object getConstantValue() {
        return null;
    }

    @Override
    public TypeMirror asType() {
        return new DeclaredTypeCv(field.getDeclaringClass());
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.FIELD;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return new ListCv<>(
                Arrays.asList(field.getDeclaredAnnotations()),
                new Conversor<Annotation, AnnotationMirror>() {
                    Annotation annotation;
                    @Override
                    public void setOriginal(Annotation original) {
                        annotation = original;
                    }

                    @Override
                    public AnnotationMirror getConverted() {
                        AnnotationMirrorCv annotationMirror = new AnnotationMirrorCv();
                        annotationMirror.setOriginal(annotation);
                        return annotationMirror;
                    }
                }
        );
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        Annotation annotation = field.getAnnotation(annotationType);
        if (annotation == null) {
            return null;
        }
        return AnnotationProxy.createProxy(annotation);
    }

    @Override
    public Set<Modifier> getModifiers() {
        return null;
    }

    @Override
    public Name getSimpleName() {
        return new NameCv(field.getName());
    }

    @Override
    public Element getEnclosingElement() {
        return null;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return null;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return null;
    }
}
