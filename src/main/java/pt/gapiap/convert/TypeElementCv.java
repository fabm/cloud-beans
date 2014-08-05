package pt.gapiap.convert;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TypeElementCv implements TypeElement {
    Class<?> clazz;

    public TypeElementCv(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public int hashCode() {
        return clazz.getCanonicalName().length();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TypeElementCv)) {
            return false;
        }
        return ((TypeElementCv) obj).clazz == clazz;
    }

    private void init() {
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        List<Element> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            list.add(new VariableElementCv(field));
        }
        for (Method method : clazz.getDeclaredMethods()) {
            list.add(new ExecElementCv(method));
        }
        return list;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return null;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Name getQualifiedName() {
        return null;
    }

    @Override
    public TypeMirror asType() {
        return new DeclaredTypeCv(clazz);
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.CLASS;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return new ListCv(Arrays.asList(clazz.getDeclaredAnnotations()), new AnnotationMirrorCv());
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return AnnotationProxy.createProxy(clazz.getAnnotation(annotationType));
    }

    @Override
    public Set<Modifier> getModifiers() {
        return null;
    }

    @Override
    public Name getSimpleName() {
        Name name = new Name() {
            private String sName;

            @Override
            public boolean contentEquals(CharSequence cs) {
                return sName.contentEquals(cs);
            }

            @Override
            public int length() {
                return sName.length();
            }

            @Override
            public char charAt(int index) {
                return sName.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return sName.subSequence(start, end);
            }

            @Override
            public String toString() {
                return sName;
            }
        };
        return name;
    }

    @Override
    public TypeMirror getSuperclass() {
        return new TypeElementCv(clazz.getSuperclass()).asType();
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        return null;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return null;
    }

    @Override
    public Element getEnclosingElement() {
        return null;
    }
}
