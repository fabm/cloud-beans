package pt.gapiap.cloudEndpoints.parameter.evaluation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Deprecated
/**
 * Use {@link pt.gapiap.proccess.validation.bean.checker.BeanChecker} instead
 */
public @interface Evaluation {
    String name() default "";
    String alias() default "";
    String[] validations() default {};
}
