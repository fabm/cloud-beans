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

public class Dispatcher<R extends Enum<R>> {
    private Object service;
    private Class<? extends Annotation> rolesAnnotationClass;
    private Authorization<R, ?> authorization;

    public Dispatcher(Object service, Class<? extends Annotation> rolesAnnotationClass, Authorization<R, ?> authorization) {
        this.service = service;
        this.rolesAnnotationClass = rolesAnnotationClass;
        this.authorization = authorization;
    }

    private CEReturn dispatch(String apiMethod, Object entry) throws UnauthorizedException {
        Class<?> classService = service.getClass();
        Method[] methods = classService.getMethods();
        Method method = null;
        MappedAction mappedAction = null;
        for (Method me : methods) {
            mappedAction = me.getAnnotation(MappedAction.class);
            if (mappedAction != null && mappedAction.value().equals(apiMethod)) {
                method = me;
                break;
            }
        }

        //TODO trocar por um erro em condições
        if (mappedAction == null) {
            throw new RuntimeException("Não mapeada");
        }

        BeanChecker beanChecker = new BeanChecker();
        beanChecker.check(entry);

        if (method != null) {
            try {
                Annotation rolesAnnotation = method.getAnnotation(rolesAnnotationClass);
                Method valueMethod;
                if (rolesAnnotation != null) {
                    valueMethod = rolesAnnotation.annotationType().getDeclaredMethod("value");
                    authorization.check((R[]) valueMethod.invoke(rolesAnnotation), mappedAction.area());
                }

                return (CEReturn) method.invoke(service, entry);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("There's no method annotation with " + MappedAction.class.getCanonicalName() +
                " for de class " + classService.getCanonicalName());
    }

    public CEReturn dispatch(Object entry) throws CEError, UnauthorizedException {
        Class<?> entryClass = entry.getClass();
        ApiMethodParameters apiMethodParameters = entryClass.getAnnotation(ApiMethodParameters.class);
        if (apiMethodParameters == null) {
            throw new RuntimeException("The entry don't have the annotation " + apiMethodParameters.getClass().getCanonicalName());
        }
        return dispatch(apiMethodParameters.method(), entry);
    }

    public CEReturn dispatch(String method) throws CEError, UnauthorizedException {
        return dispatch(method, null);
    }


}
