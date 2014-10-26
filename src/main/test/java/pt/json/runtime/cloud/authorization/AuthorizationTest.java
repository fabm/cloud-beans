package pt.json.runtime.cloud.authorization;

import pt.gapiap.cloud.endpoints.authorization.AppEnvironment;
import pt.gapiap.cloud.endpoints.authorization.Authorization;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationTest extends Authorization<MyRole> {

  MessageFormat template = new MessageFormat("loadUserWithRoles({0})");
  private boolean saved = false;
  private UserTest userTest;
  private boolean production = false;
  private List<String> callsList = new ArrayList<>();


  @Override
  public AppEnvironment getAppEnvironment() {
    return new AppEnvironment() {
      @Override
      public boolean isProductionMode() {
        return AuthorizationTest.this.production;
      }

      @Override
      public boolean isDevMode() {
        return !AuthorizationTest.this.production;
      }
    };
  }

  public void setUserTest(UserTest userTest) {
    this.userTest = userTest;
  }

  public void setSaved(boolean saved) {
    this.saved = saved;
  }

}
