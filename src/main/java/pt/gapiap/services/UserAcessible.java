package pt.gapiap.services;

import com.google.appengine.api.users.User;
import pt.gapiap.cloud.endpoints.Authorization;

public interface UserAcessible {
    void setUser(User user);
    Authorization getAuthorization();
}

