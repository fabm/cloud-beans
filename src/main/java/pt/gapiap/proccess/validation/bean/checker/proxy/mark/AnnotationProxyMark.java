package pt.gapiap.proccess.validation.bean.checker.proxy.mark;

import java.lang.reflect.InvocationHandler;
import java.util.Map;
import java.util.Set;

public interface AnnotationProxyMark<T> extends InvocationHandler {
    T getProxy();
    Map<String,?> valuesMap();
}
