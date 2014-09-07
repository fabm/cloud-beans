package pt.json.runtime.cloud.authorization;

import pt.gapiap.cloud.endpoints.authorization.AppEnvironment;
import pt.gapiap.cloud.endpoints.authorization.Authorization;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationTest extends Authorization<MyRole, UserTest> {

  MessageFormat template = new MessageFormat("loadDataStore({0})");
  private boolean saved = false;
  private UserTest userTest;
  private boolean production = false;
  private List<String> callsList = new ArrayList<>();

  @Override
  protected void loadDataStore(String email) {
    callsList.add(template.format(email));
  }

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

  /**
   * If the user is not persisted this call must be an implementation of persistence
   *
   * @return {@linkplain pt.json.runtime.cloud.authorization.UserTest}
   */
  @Override
  public UserTest savedUser() {
    saved = true;
    return userTest;
  }

  @Override
  protected void devMode() {
    callsList.add("devMode()");
  }

  public void setUserTest(UserTest userTest) {
    this.userTest = userTest;
  }

  public void setSaved(boolean saved) {
    this.saved = saved;
  }

}
