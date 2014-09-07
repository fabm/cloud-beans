package pt.json.runtime;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.Test;
import pt.gapiap.errors.ErrorManager;
import pt.gapiap.proccess.validation.LocaleFieldName;
import pt.gapiap.proccess.validation.bean.checker.BeanChecker;
import pt.gapiap.proccess.validation.bean.checker.FailedField;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorArea;
import pt.json.proccess.test.examples.AnnotatedObject;
import pt.json.runtime.cloud.services.ServiceTest;
import pt.json.runtime.cloud.services.TestRequest1;

import java.util.HashMap;
import java.util.Map;

public class RuntimeTests {

  Module guiceModule = new AbstractModule() {
    @Override
    protected void configure() {
      ErrorManager errorManager = new ErrorManager();
      DefaultValidatorErrorArea defaultValidatorErrorArea = new DefaultValidatorErrorArea(errorManager);


      //singletons
      bind(ErrorManager.class).toInstance(errorManager);
      bind(DefaultValidatorErrorArea.class).toInstance(defaultValidatorErrorArea);
    }
  };

  LocaleFieldName localeFieldName = new LocaleFieldName() {

    Map<String, String> map = new HashMap<String, String>() {{
      put("testeInt", "'teste inteiro'");
    }};

    private String getString(final String key) {
      Optional<String> op = Optional.fromNullable(map.get(key));
      return op.or(key);
    }

    @Override
    public String getLocaleFildName(String fieldName, Class<?> baseClass, String language) {
      return getString(fieldName);
    }
  };

  @Test
  public void beanCheck() {
    Injector injector = Guice.createInjector(guiceModule);
    AnnotatedObject annotatedObject = new AnnotatedObject();
    annotatedObject.setEmail("badEmailFromat");

    BeanChecker beanChecker = new BeanChecker(true);
    injector.injectMembers(beanChecker);

    AnnotatedObject checked = beanChecker.check(annotatedObject);

    for (FailedField failedField : beanChecker.getFailedFields()) {
      System.out.println(failedField.getMessage());
    }
  }

  @Test
  public void beanCheckWithLanguage() {
    Injector injector = Guice.createInjector(guiceModule);
    AnnotatedObject annotatedObject = new AnnotatedObject();
    annotatedObject.setEmail("badEmailFromat");

    BeanChecker beanChecker = new BeanChecker(true, "pt", localeFieldName);
    injector.injectMembers(beanChecker);

    AnnotatedObject checked = beanChecker.check(annotatedObject);

    for (FailedField failedField : beanChecker.getFailedFields()) {
      System.out.println(failedField.getMessage());
    }
  }

  @Test
  public void callUsersList() {
    Injector injector = Guice.createInjector(guiceModule);
    ServiceTest serviceTest = injector.getInstance(ServiceTest.class);


    TestRequest1 testRequest1 = new TestRequest1();
    testRequest1.setTestIntRequest1(2);


    serviceTest.testMethod1(testRequest1);
  }

}
