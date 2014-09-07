package pt.json.runtime.cloud.services;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;

import javax.validation.constraints.NotNull;

@ApiMethodParameters(api = "apiTest", method = "apiMethod1", validators = DefaultValidator.class)
public class TestRequest1 {
  @NotNull
  private Integer testIntRequest1;
  @NotNull
  private String testStringRequest1;

  public String getTestStringRequest1() {
    return testStringRequest1;
  }

  public void setTestStringRequest1(String testStringRequest1) {
    this.testStringRequest1 = testStringRequest1;
  }

  public int getTestIntRequest1() {

    return testIntRequest1;
  }

  public void setTestIntRequest1(int testIntRequest1) {
    this.testIntRequest1 = testIntRequest1;
  }
}
