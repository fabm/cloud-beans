package pt.json.runtime.cloud.authorization;

import com.google.appengine.api.users.User;
import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;

import java.util.Set;

public class UserTest implements UserWithRoles<MyRole> {

  private Set<MyRole> myMyRoles;

  @Override
  public Set<MyRole> getRoles() {
    return myMyRoles;
  }

  @Override
  public void setUser(User user) {
    //TODO colocar user do metodo
  }

  public void setMyMyRoles(Set<MyRole> myMyRoles) {
    this.myMyRoles = myMyRoles;
  }
}
