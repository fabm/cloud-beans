package pt.gapiap.proccess.validation;

import pt.gapiap.errors.ErrorArea;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ValidationClass {
    Class<? extends ErrorArea> value();
}
