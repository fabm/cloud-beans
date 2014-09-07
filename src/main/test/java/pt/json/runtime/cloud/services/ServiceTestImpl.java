package pt.json.runtime.cloud.services;

public class ServiceTestImpl implements ServiceTest {

  @Override
  public Object testMethod1(TestRequest1 testRequest1) {
    int testIntRequest1 = testRequest1.getTestIntRequest1();
    String testStringRequest1 = testRequest1.getTestStringRequest1();
    return testIntRequest1+":"+testIntRequest1;
  }

  @Override
  public Object testMethod2() {
    return "testMethod2";
  }

  @Override
  public Object testNoRole() {
    return "testNoRole";
  }

  @Override
  public Object testNeedsAuthentication() {
    return "testNeedsAuthentication";
  }
}
