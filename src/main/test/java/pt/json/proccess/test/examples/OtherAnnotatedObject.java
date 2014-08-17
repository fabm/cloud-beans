package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;
import pt.gapiap.proccess.validation.annotations.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiMethodParameters(api = "thisApi", method = "myMethod", validators = DefaultValidator.class)
public class OtherAnnotatedObject {
    @NotNull
    @Email
    String emailRequired;

    @Size(min = 2, max = 4)
    int myInt;
}
