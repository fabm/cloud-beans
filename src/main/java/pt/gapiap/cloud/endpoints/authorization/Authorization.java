package pt.gapiap.cloud.endpoints.authorization;


import com.google.inject.Inject;
import pt.gapiap.guice.CurrentEmailProvider;

import javax.annotation.Nullable;


public class Authorization<R extends Enum<R>> {
  @Inject
  @Nullable
  private UserWithRoles<R> user;
  @Inject
  private AppEnvironment appEnvironment;


  @Inject
  private void init(CurrentEmailProvider currentEmailProvider) {
    if (appEnvironment.isProductionMode() && currentEmailProvider != null && user != null) {
      user.setEmail(currentEmailProvider.get());
    }
  }


  public boolean hasRole(R roleRequired) {
    if(user == null){
        return false;
    }
    if (user.getRoles() == null) {
      return false;
    }
    for (R role : user.getRoles()) {
      if (role == roleRequired) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param rolesRequired
   * @return true if all rolesRequired are in user roles
   */
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
}
