package pt.gapiap.cloud.endpoints;


import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.utils.SystemProperty;


public abstract class Authorization<R extends Enum<R>, U extends UserWithRoles<R>> {

    protected U userWithRoles;
    protected boolean userRegistered = false;

    private Authorization() {

    }

    public Authorization(User user) {
        if (user != null) {
            this.init(user);
        } else {
            this.init(null);
        }
    }

    protected UnauthorizedException createNotAuthenticatedError(String area) {
        return new UnauthorizedException(String.format(GlobalError.NOT_AUTHENTICATED.getMsg(), area));
    }

    protected UnauthorizedException createNotAuthorizedError(String area) {
        return new UnauthorizedException(String.format(GlobalError.NOT_AUTHORIZED.getMsg(), area));
    }

    public void checkDevMode() {
        if (SystemProperty.Environment.Value.Development != SystemProperty.Environment.Value.Development) {
            createNotAuthenticatedError("only authorized in devmode");
        }
    }

    public U getUserWithRoles() {
        return userWithRoles;
    }

    protected abstract void loadDataStore(String email);

    private void init(User user) {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
            devMode();
            return;
        }
        if (user == null) {
            return;
        }
        loadDataStore(user.getEmail());
    }

    public boolean isUserRegistered() {
        return userRegistered;
    }


    public <T extends Enum<T>> boolean hasRole(T roleRequired) {
        if (this.userWithRoles.getRoles() == null) {
            return false;
        }
        for (Enum<?> role : this.userWithRoles.getRoles()) {
            if (role == roleRequired) {
                return true;
            }
        }
        return false;
    }


    public abstract U savedUserFromApp() throws CEError;

    protected abstract void devMode();

    protected boolean hasRoles(R[] rolesRequired) {
        if (rolesRequired == null || rolesRequired.length == 0) {
            return true;
        }
        for (R roleRequired : rolesRequired) {
            if (hasRole(roleRequired)) {
                return true;
            }
        }
        return false;
    }


    public void check(R[] roles, String area) throws UnauthorizedException {
        if (userWithRoles == null) {
            throw createNotAuthenticatedError(area);
        }
        if (!hasRoles(roles)) {
            throw createNotAuthorizedError(area);
        }
    }
}
