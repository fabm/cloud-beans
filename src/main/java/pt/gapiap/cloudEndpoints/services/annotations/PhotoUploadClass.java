package pt.gapiap.cloudEndpoints.services.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PhotoUploadClass {
    InstanceType type();
}

