package pt.gapiap.services;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import pt.gapiap.cloud.endpoints.Authorization;
import pt.gapiap.cloud.endpoints.CEError;
import pt.gapiap.cloud.endpoints.CEReturn;

public class CEDispatcher<R extends Enum<R>> implements CEReturn {
    private Dispatcher<R> dispatcher;
    private Object entry;
    private String methodName;
    private Authorization<R,?> authorization;

    public CEDispatcher(Dispatcher<R> dispatcher) {
        this.dispatcher = dispatcher;
    }

    public CEDispatcher<R> setAuthorization(Authorization<R, ?> authorization) {
        this.authorization = authorization;
        return this;
    }

    public Object getEntry() {
        return entry;
    }

    public CEDispatcher<R> setEntry(Object entry) {
        this.entry = entry;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public CEDispatcher<R> setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Authorization<R, ?> getAuthorization() {
        return authorization;
    }

    @Override
    public Object getCEResponse() throws CEError, UnauthorizedException {
        dispatcher.injectFields(authorization);
        return dispatcher.dispatch(authorization,methodName,entry);
    }
}
