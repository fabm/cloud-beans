package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;

import javax.validation.constraints.NotNull;

@ApiMethodParameters(api = "testApi", method = "um.dois.quatro.tres", validators = DefaultValidator.class)
public class AnnotatedObject {


  @NotNull
  private Integer testeInt;
  @Email
  private String email;

  public Integer getTesteInt() {
    return testeInt;
  }

  public void setTesteInt(Integer testeInt) {
    this.testeInt = testeInt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
