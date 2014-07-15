package pt.gapiap.cloud.endpoints;

import com.google.appengine.api.users.User;

import java.lang.Enum;import java.util.Set;

public interface UserWithRoles<T extends Enum<T>> {
    Set<T> getRoles();
}
