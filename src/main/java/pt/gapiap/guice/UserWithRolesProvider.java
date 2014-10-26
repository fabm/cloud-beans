package pt.gapiap.guice;

import com.google.inject.Provider;
import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;

import javax.annotation.Nullable;

public interface UserWithRolesProvider<R extends Enum<R>> extends Provider<UserWithRoles<R>>{
  void setEmail(@Nullable String email);
}
