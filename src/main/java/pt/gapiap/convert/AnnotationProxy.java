package pt.gapiap.convert;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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
        if (method.getReturnType().isArray() && method.getReturnType().getComponentType() == Class.class) {
            Class<?>[] classes = (Class<?>[]) method.invoke(annotation, args);
            List<TypeMirror> typeMirrors = new ArrayList<>(classes.length);
            for(Class<?> clazz:classes){
                typeMirrors.add(new DeclaredTypeCv(clazz));
            }
            throw new MirroredTypesException(typeMirrors);
        }
        Object rObject = method.invoke(annotation, args);
        return rObject;
    }
}
