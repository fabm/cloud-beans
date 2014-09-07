package pt.gapiap.cloud.endpoints.authorization;

import java.lang.annotation.Annotation;

public interface RolesCapturer <R extends Enum<R>,A extends Annotation>{
    Class<A> getAnnotationClass();
    R[] getRoles(A annotation);
}
