package pt.json.runtime.cloud.authorization;

import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;

import java.util.Set;

public class UserTest implements UserWithRoles<MyRole> {

  private Set<MyRole> myMyRoles;

  @Override
  public Set<MyRole> getRoles() {
    return myMyRoles;
  }

  public void setMyMyRoles(Set<MyRole> myMyRoles) {
    this.myMyRoles = myMyRoles;
  }
}
