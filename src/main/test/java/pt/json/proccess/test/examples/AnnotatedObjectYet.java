package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.DefaultValidator;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Required;

@ApiMethodParameters(api = "testApi",method = "test.method.first.one",validator = DefaultValidator.class)
public class AnnotatedObjectYet {
    @Required
    private Integer testeInt;
    @Email
    private String email;
}
