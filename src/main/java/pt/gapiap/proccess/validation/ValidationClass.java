package pt.gapiap.proccess.validation;

import pt.gapiap.cloud.endpoints.errors.ErrorArea;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ValidationClass {
    Class<? extends ErrorArea> value();
}
