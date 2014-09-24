package pt.gapiap.runtime.reflection;

import java.lang.annotation.Annotation;

public interface EnumArrayFromAnnotation<E extends Enum<E>,A extends Annotation>{
    Class<A> getAnnotationClass();
    E[] getEnumArray(A annotation);
}
