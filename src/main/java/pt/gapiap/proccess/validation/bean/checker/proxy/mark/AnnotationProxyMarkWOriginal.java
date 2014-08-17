package pt.gapiap.proccess.validation.bean.checker.proxy.mark;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProxyMarkWOriginal<T extends Annotation> implements AnnotationProxyMark<T> {
    Map<String, Object> map;
    private T original;
    private T proxy;

    public AnnotationProxyMarkWOriginal(T original, Map<String,Object> map, Class<?>... classes) {
        this.original = original;
        this.map = map;
        init(classes);
    }

    private void init(Class<?>[] classes) {
        if (classes.length == 0) {
            classes = new Class[]{original.annotationType()};
        }
        proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), classes, this);
    }


    @Override
    public T getProxy() {
        return proxy;
    }

    @Override
    public Map<String, ?> valuesMap() {
        return map;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object value = method.invoke(original, args);
        map.put(method.getName(),(Object)value);
        return value;
    }
}
