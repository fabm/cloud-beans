package pt.gapiap.cloud.endpoints.authorization;

import com.google.appengine.api.users.User;

import java.util.Set;

public interface UserWithRoles<T extends Enum<T>> {
  Set<T> getRoles();
  void setUser(User user);
}
