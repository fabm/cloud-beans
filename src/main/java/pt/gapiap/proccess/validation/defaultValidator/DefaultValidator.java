package pt.gapiap.proccess.validation.defaultValidator;

import pt.gapiap.proccess.validation.EmailChecker;
import pt.gapiap.proccess.validation.ValidationMethod;
import pt.gapiap.proccess.validation.annotations.DateBetween;
import pt.gapiap.proccess.validation.annotations.DateFuture;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.bean.checker.ValidationContext;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContent;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

//todo ver no validator.ts os valores que vêm dentro do objecto json
public class DefaultValidator {

  private boolean nullable = true;

  private static Calendar toCalendar(int year, int month, int day) {
    return new GregorianCalendar(year, month, day);
  }

  private static boolean isACalendarType(Object value) {
    return value.getClass().isAssignableFrom(Calendar.class);
  }

  @ValidationMethod(value = NotNull.class, priority = 1, failCode = DefaultValidatorErrorContent.NOT_NULL)
  public boolean valRequired(ValidationContext<NotNull> context) {
    nullable = false;
    if (!(context.isNull() || context.isEmptyString())) {
      return true;
    }

    context.failArgs().add(context.getLocalFieldName());

    return false;
  }

  @ValidationMethod(value = Email.class, failCode = DefaultValidatorErrorContent.EMAIL)
  public boolean valEmail(ValidationContext<Email> context) {
    if (context.isAPermittedNull(nullable) || EmailChecker.check(context.getValue())) {
      return true;
    }

    context.failArgs().add(context.getLocalFieldName());

    return false;
  }

  private int valueForSize(ValidationContext<?> context) {
    if (context.isCollection()) {
      Collection<?> colletion = (Collection<?>) context.getValue();
      return colletion.size();
    } else if (context.isArray()) {
      return Array.getLength(context.getValue());
    } else if (context.isString()) {
      return context.getValue().toString().length();
    } else {
      return (Integer) context.getValue();
    }
  }

  @ValidationMethod(value = Size.class, failCode = DefaultValidatorErrorContent.SIZE)
  public boolean valSize(ValidationContext<Size> context) {
    if (context.isAPermittedNull(nullable)) {
      return true;
    }
    int value = valueForSize(context);

    Size size = context.getAnnotation();

    if (value > size.min() && value < size.max()) {
      return true;
    }

    Collection<Object> failArgs = context.failArgs();

    failArgs.add(context.getName());
    failArgs.add(size.min());
    failArgs.add(size.max());

    return false;
  }

  @ValidationMethod(value = Min.class, failCode = DefaultValidatorErrorContent.MIN)
  public boolean valMin(ValidationContext<Min> context) {
    if (context.isAPermittedNull(nullable)) {
      return true;
    }
    int value = valueForSize(context);

    if (value > context.getAnnotation().value()) {
      return true;
    }

    Collection<Object> failArgs = context.failArgs();

    failArgs.add(context.getLocalFieldName());
    failArgs.add(context.getAnnotation().value());

    return false;
  }

  @ValidationMethod(value = Max.class, failCode = DefaultValidatorErrorContent.MAX)
  public boolean valMax(ValidationContext<Max> context) {
    if (context.isAPermittedNull(nullable)) {
      return true;
    }
    int value = valueForSize(context);

    Max annotation = context.getAnnotation();

    if (value < annotation.value()) {
      return true;
    }

    context.failArgs().add(context.getLocalFieldName());
    context.failArgs().add(annotation.value());

    return false;
  }

  //todo complete
  @ValidationMethod(value = DateFuture.class, failCode = DefaultValidatorErrorContent.DATE_FUTURE)
  public boolean valDateFuture(ValidationContext<DateFuture> context) {
    if (context.isAPermittedNull(nullable)) {
      return true;
    }
    if(!isACalendarType(context.getValue())){
      return false;
    }


    return false;
  }


  //todo complete
  @ValidationMethod(value = DateBetween.class, failCode = DefaultValidatorErrorContent.DATE_BETWEEN)
  public boolean valDateBetween(ValidationContext<DateBetween> context) {

    DateBetween annotation = context.getAnnotation();

    if(!isACalendarType(context.getValue())){
      return false;
    }
    if (context.isAPermittedNull(nullable)) {
      return true;
    }

    Calendar begin = toCalendar(annotation.beginYear(), annotation.beginMonth(), annotation.beginDay());
    Calendar end = toCalendar(annotation.beginYear(), annotation.beginMonth(), annotation.beginDay());


    return end.before(context.getValue()) && begin.after(context.getValue());
  }
}
