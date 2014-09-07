package pt.gapiap.proccess.validation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ValidationMethod {
    Class<? extends Annotation> value();
    int priority() default 0;
    int failCode();
}
