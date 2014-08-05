package pt.gapiap.convert;

import javax.lang.model.type.MirroredTypeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationProxy implements InvocationHandler {

    private Annotation annotation;

    public AnnotationProxy(Annotation annotation) {
        this.annotation = annotation;
    }

    public static <T extends Annotation> T createProxy(Annotation annotation) {
        Class<?> clazz = annotation.getClass();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader()
                , clazz.getInterfaces(),
                new AnnotationProxy(annotation));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getReturnType() == Class.class) {
            Class<?> clazz = (Class<?>) method.invoke(annotation, args);
            throw new MirroredTypeException(new DeclaredTypeCv(clazz));
        }
        Object rObject = method.invoke(annotation, args);
        return rObject;
    }
}
