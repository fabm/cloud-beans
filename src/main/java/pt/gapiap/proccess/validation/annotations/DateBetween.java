package pt.gapiap.proccess.validation.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Calendar;

//todo validar em tempo de compilação
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
public @interface DateBetween {
  int beginDay();
  int beginMonth();
  int beginYear();

  int endDay();
  int endMonth();
  int endYear();
}
