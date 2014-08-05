package pt.json.proccess.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyContainer<T> implements InvocationHandler {

    private T original;
    private ProxyController proxyController;

    public ProxyContainer(T original, ProxyController proxyController) {
        this.original = original;
        this.proxyController = proxyController;
    }

    public static <T>T createProxy(T original, ProxyController proxyController) {
        Class<?> clazz = original.getClass();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new ProxyContainer(original, proxyController));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (proxyController.isTrueCall(method.getName())) {
            return method.invoke(original, args);
        }
        return proxyController.getResponse(method.getName());
    }
}
