package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;

import javax.validation.constraints.NotNull;

@ApiMethodParameters(api = "testApi", method = "um.dois.quatro.quatro", validators = DefaultValidator.class)
public class AnnotatedObjectYet {
  @NotNull
  private Integer testeInt;

  @Email
  private String email;
}
