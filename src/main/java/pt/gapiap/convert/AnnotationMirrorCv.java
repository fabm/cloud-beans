package pt.gapiap.convert;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMirrorCv implements AnnotationMirror, Conversor<Annotation, AnnotationMirror> {
    private Annotation annotation;

    @Override
    public DeclaredType getAnnotationType() {
        return new DeclaredTypeCv(annotation.annotationType());
    }

    @Override
    public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValues() {
        Map<ExecutableElement, AnnotationValue> map = new HashMap<>();
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            try {
                map.put(new ExecElementCv(method), new AnnotationValueCv(method.invoke(annotation)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    @Override
    public void setOriginal(Annotation original) {
        annotation = original;
    }


    @Override
    public AnnotationMirror getConverted() {
        return this;
    }

}
