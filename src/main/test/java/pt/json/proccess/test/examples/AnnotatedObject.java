package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;
import pt.gapiap.proccess.validation.annotations.Email;

import javax.validation.constraints.NotNull;

@ApiMethodParameters(api = "testApi",method = "um.dois.quatro.tres",validators = DefaultValidator.class)
public class AnnotatedObject {
    @NotNull
    private Integer testeInt;
    @Email
    private String email;
}
