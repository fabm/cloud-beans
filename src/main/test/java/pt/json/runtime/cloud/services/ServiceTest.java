package pt.json.runtime.cloud.services;

import pt.json.runtime.cloud.acl.MyRolesValidation;
import pt.json.runtime.cloud.authorization.MyRole;

public interface ServiceTest {
  @MyRolesValidation(MyRole.ROLE1)
  Object testMethod1(TestRequest1 testRequest1);

  @MyRolesValidation(MyRole.ROLE2)
  Object testMethod2();

  Object testNoRole();

  @MyRolesValidation
  Object testNeedsAuthentication();
}
