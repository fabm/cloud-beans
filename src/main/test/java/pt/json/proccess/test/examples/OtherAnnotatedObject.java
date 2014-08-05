package pt.json.proccess.test.examples;

import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.DefaultValidator;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Required;
import pt.gapiap.proccess.validation.annotations.Size;

@ApiMethodParameters(api = "thisApi", method = "myMethod", validator = DefaultValidator.class)
public class OtherAnnotatedObject {
    @Required
    @Email
    String emailRequired;

    @Size(min = 2, max = 4)
    int myInt;
}
