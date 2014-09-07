package pt.gapiap.cloud.endpoints.authorization;


import com.google.appengine.api.users.User;
import com.google.appengine.api.utils.SystemProperty;


public abstract class Authorization<R extends Enum<R>, U extends UserWithRoles<R>> {

  protected U userWithRoles;
  protected boolean userPersisted = false;
  private String language = "en";
  private AppEnvironment appEnvironment = new AppEnvironment() {
    @Override
    public boolean isProductionMode() {
      return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
    }

    @Override
    public boolean isDevMode() {
      return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
    }
  };

  /**
   * @return AppEnvironment
   * {@linkplain com.google.appengine.api.utils.SystemProperty.Environment.Value} comparative with
   * {@code SystemProperty.environment.value()}
   */
  public AppEnvironment getAppEnvironment() {
    return appEnvironment;
  }

  /**
   * Sets the user language
   *
   * @param language
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * @return {@linkplain U}
   */
  public U getUserWithRoles() {
    return userWithRoles;
  }

  /**
   * will be called if not in
   *
   * @param email
   */
  protected abstract void loadDataStore(String email);

  /**
   * inits the Authorization bean
   *
   * @param user
   */
  public void init(User user) {
    if (appEnvironment.isDevMode()) {
      devMode();
      return;
    }
    if (user == null) {
      return;
    }
    loadDataStore(user.getEmail());
  }


  public boolean isUserPersisted() {
    return userPersisted;
  }


  public <T extends Enum<T>> boolean hasRole(T roleRequired) {
    if (this.userWithRoles.getRoles() == null) {
      return false;
    }
    for (R role : this.userWithRoles.getRoles()) {
      if (role == roleRequired) {
        return true;
      }
    }
    return false;
  }

  /**
   * If the user is not persisted this call must be an implementation of persistence
   *
   * @return {@linkplain U}
   */
  public abstract U savedUser();

  /**
   * this method will called if {@linkplain AppEnvironment#isDevMode()} is true
   */
  protected abstract void devMode();

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
