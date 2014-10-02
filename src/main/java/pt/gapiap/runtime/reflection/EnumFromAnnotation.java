package pt.gapiap.runtime.reflection;

import java.lang.annotation.Annotation;

public interface EnumFromAnnotation<E extends Enum<E>,A extends Annotation>{
    Class<A> getAnnotationClass();
    E getEnum(A annotation);
}
