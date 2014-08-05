package pt.gapiap.convert;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ExecElementCv implements ExecutableElement {
    private Method method;

    public ExecElementCv(Method method) {
        this.method = method;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return null;
    }

    @Override
    public TypeMirror getReturnType() {
        return new DeclaredTypeCv(method.getReturnType());
    }

    @Override
    public List<? extends VariableElement> getParameters() {
        return null;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public List<? extends TypeMirror> getThrownTypes() {
        return null;
    }

    @Override
    public AnnotationValue getDefaultValue() {
        return null;
    }

    @Override
    public TypeMirror asType() {
        return null;
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.METHOD;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        List<Annotation> original = Arrays.asList(method.getDeclaredAnnotations());
        AnnotationMirrorCv annotationMirrorCv = new AnnotationMirrorCv();
        ListCv<Annotation, AnnotationMirror> listCv = new ListCv<>(original, annotationMirrorCv);
        return listCv;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        Annotation annotation = method.getAnnotation(annotationType);
        if (annotation == null) {
            return null;
        }
        return AnnotationProxy.createProxy(annotation);
    }

    @Override
    public Set<Modifier> getModifiers() {
        ModifierCv modifierCv = new ModifierCv();
        modifierCv.setOriginal(method.getModifiers());
        return modifierCv.getConverted();
    }

    @Override
    public Name getSimpleName() {
        return new NameCv(method.getName());
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
