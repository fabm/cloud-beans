package pt.gapiap.convert;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * RoundEnvironment class annotated based
 */
public class RoundEnvironmentCAB implements RoundEnvironment {
    private List<? extends Class<?>> list;
    private Map<Class<? extends Annotation>, Set<Class<?>>> annotationsMap;

    public RoundEnvironmentCAB(List<? extends Class<?>> list) {
        annotationsMap = new HashMap<>();
        this.list = list;
        init();
    }

    private void init() {
        for (Class<?> clazz : list) {
            annotationsDiscovery(clazz, clazz.getAnnotations());
        }
    }

    private void annotationsDiscovery(Class<?> clazz, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Set<Class<?>> classList = annotationsMap.get(annotation);
            if (classList == null) {
                classList = new HashSet<>();
                annotationsMap.put(annotation.annotationType(), classList);
            }
            classList.add(clazz);
        }
    }

    @Override
    public boolean processingOver() {
        return false;
    }

    @Override
    public boolean errorRaised() {
        return false;
    }

    @Override
    public Set<? extends Element> getRootElements() {
        return null;
    }

    @Override
    public Set<? extends Element> getElementsAnnotatedWith(TypeElement a) {
        if (!(a instanceof TypeElementCv)) {
            throw new ClassCastException(
                    "Parameter a must be a " + TypeElementCv.class.getCanonicalName()
            );
        }
        TypeElementCv acv = (TypeElementCv) a;
        if (!acv.clazz.isAssignableFrom(Annotation.class)) {
            throw new ClassCastException("a.clazz must be a Class<? extends Annotation>");
        }
        Set<Class<?>> classSet = annotationsMap.get(acv.clazz);
        if (classSet == null) {
            return new HashSet<>();
        }
        Set<Element> elementSet = new HashSet<>();
        for (Class<?> clazz : classSet) {
            elementSet.add(new TypeElementCv(clazz));
        }
        return elementSet;
    }

    @Override
    public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
        Set<Element> elementSet = new HashSet<>();
        for(Class<?> clazz:list){
            if(clazz.getAnnotation(a)!=null){
                elementSet.add(new TypeElementCv(clazz));
            }
        }
        return elementSet;
    }
}
