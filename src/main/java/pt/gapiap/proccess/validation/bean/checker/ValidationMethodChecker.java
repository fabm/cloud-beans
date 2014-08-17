package pt.gapiap.proccess.validation.bean.checker;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ValidationMethodChecker extends Comparable<ValidationMethodChecker>{
    Class<? extends Annotation> annotationValidation();
    int getPriority();
    Map<String,?> checkValidation(ValidationContext<?> validationContext);
}
