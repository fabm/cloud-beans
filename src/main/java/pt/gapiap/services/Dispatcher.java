package pt.gapiap.services;

import com.google.api.server.spi.response.UnauthorizedException;
import pt.gapiap.cloud.endpoints.Authorization;
import pt.gapiap.cloud.endpoints.CEError;
import pt.gapiap.cloud.endpoints.CEReturn;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.annotations.MappedAction;
import pt.gapiap.proccess.validation.BeanChecker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher<R extends Enum<R>> {
    private Object service;
    private Class<? extends Annotation> rolesAnnotationClass;
    private Authorization<R, ?> authorization;
    private Map<Class<?>, String> classMapMehods;
    private Map<String, Method> stringMapMethods;
    private Map<String, AuthorizationArea> authorizationAreaMap;


    public Dispatcher(Object service, Class<? extends Annotation> rolesAnnotationClass) {
        this.service = service;
        this.rolesAnnotationClass = rolesAnnotationClass;
        init();
    }

    private void init() {
        classMapMehods = new HashMap<>();
        stringMapMethods = new HashMap<>();

        Class<?> classService = service.getClass();
        Method[] methods = classService.getMethods();
        Method method = null;
        MappedAction mappedAction = null;
        for (Method me : methods) {
            mappedAction = me.getAnnotation(MappedAction.class);
            if (mappedAction != null) {
                stringMapMethods.put(mappedAction.value(), me);
            }
            Annotation rolesAnnotation = method.getAnnotation(rolesAnnotationClass);
            if (rolesAnnotation != null) {
                try {
                    Method valueMethod = rolesAnnotation.annotationType().getDeclaredMethod("value");
                    AuthorizationArea authorizationArea = new AuthorizationArea();
                    authorizationArea.setArea(mappedAction.area());
                    authorizationArea.setRoles((R[]) valueMethod.invoke(rolesAnnotation));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private CEReturn dispatch(String methodName, Method method, Object entry) throws UnauthorizedException {

        BeanChecker beanChecker = new BeanChecker();
        beanChecker.check(entry);

        authorization.check(authorizationAreaMap.get(methodName));

        try {
            if (entry == null) {
                return (CEReturn) method.invoke(service);
            } else {
                return (CEReturn) method.invoke(service, entry);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static ApiMethodParameters throwErrorIfNull(ApiMethodParameters apiMethodParameters) {
        if (apiMethodParameters == null) {
            throw new RuntimeException("The entry don't have the annotation " + ApiMethodParameters.class.getCanonicalName());
        }
        return apiMethodParameters;
    }


    public CEReturn dispatch(Object entry) throws CEError, UnauthorizedException {
        Class<?> entryClass = entry.getClass();
        String methodName = classMapMehods.get(entryClass);
        if (methodName == null) {
            ApiMethodParameters apiMethodParameters = throwErrorIfNull(entryClass.getAnnotation(ApiMethodParameters.class));
            methodName = apiMethodParameters.method();
            classMapMehods.put(entryClass, methodName);

        }
        Method method = stringMapMethods.get(methodName);

        return dispatch(methodName, method, entry);
    }

    private Method throwErrorIfMethodNull(Method method) {
        if (method == null) {
            throw new RuntimeException("There's no method annotation with " + MappedAction.class.getCanonicalName() +
                    " for de class " + service.getClass().getCanonicalName());
        }
        return method;
    }

    public CEReturn dispatch(String methodName) throws CEError, UnauthorizedException {
        return dispatch(methodName, throwErrorIfMethodNull(stringMapMethods.get(methodName)), null);
    }


    public void setAuthorization(Authorization<R, ?> authorization) {
        this.authorization = authorization;
    }
}
