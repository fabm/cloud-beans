package pt.json.proccess.test;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import pt.gapiap.cloud.maps.ApiMapper;
import pt.gapiap.convert.DeclaredTypeCv;
import pt.gapiap.convert.TypeElementCv;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.json.writer.AnProcWriters;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.bean.checker.BeanChecker;
import pt.json.proccess.test.apiMap.inject.AProcessorGMTest;
import pt.json.proccess.test.examples.AnnotatedObject;
import pt.json.proccess.test.examples.ClassWithStaticMethod;
import pt.json.proccess.test.examples.InterfaceToProxy;
import pt.json.proccess.test.examples.OtherAnnotatedObject;
import pt.json.proccess.test.guice.AnotherModuleTest;
import pt.json.proccess.test.guice.ModuleTest;
import pt.json.proccess.test.guice.MyModule;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestAnnotationProcess {

  private static TypeElement createTypeElement(Class<?> clazz) {
    return new TypeElementCv(clazz);
  }

  private static DeclaredType createDeclaredType(Class<?> clazz) {
    return new DeclaredTypeCv(clazz);
  }

  @Test
  public void uniqueTypeElements() {
    Set<DeclaredType> declaredTypes = new HashSet<>();
    Set<TypeElement> typeElements = new HashSet<>();


    declaredTypes.add(createDeclaredType(AnnotatedObject.class));
    declaredTypes.add(createDeclaredType(OtherAnnotatedObject.class));

    typeElements.add(createTypeElement(AnnotatedObject.class));
    typeElements.add(createTypeElement(OtherAnnotatedObject.class));

    assertEquals(2, declaredTypes.size());
    assertEquals(2, typeElements.size());

    declaredTypes.add(createDeclaredType(AnnotatedObject.class));
    declaredTypes.add(createDeclaredType(OtherAnnotatedObject.class));

    typeElements.add(createTypeElement(AnnotatedObject.class));
    typeElements.add(createTypeElement(OtherAnnotatedObject.class));

    assertEquals(2, declaredTypes.size());
    assertEquals(2, typeElements.size());
  }


  @Test
  public void testGuice() {
    Injector injector = Guice.createInjector(new MyModule());

    ModuleTest moduleTest = injector.getInstance(ModuleTest.class);
    assertEquals("this class is binded", moduleTest.getBindedClass().toString());
    assertEquals("class injected 1 times", moduleTest.getBindedSingletoneClass().toString());

    AnotherModuleTest anotherModuleTest = injector.getInstance(AnotherModuleTest.class);
    assertEquals("class injected 2 times", anotherModuleTest.getBindedSingletoneClass().toString());
    assertEquals("this is a inner var", anotherModuleTest.getInnerClass().getInnerVar());
  }

  @Test
  public void testValidationJson() throws IOException {
    InputStream stream = getClass().getClassLoader().getResourceAsStream("errors/en/General.json");

    BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream));

    String line;
    while ((line = streamReader.readLine()) != null) {
      System.out.println(line);
    }
  }

  @Test
  public void apiMap() {
    Injector injector = Guice.createInjector(new AProcessorGMTest());

    ApiMapper apiMapper = injector.getInstance(ApiMapper.class);
    apiMapper.init();

    assertNotNull(apiMapper);
    assertMethodsNames(
        ImmutableMap.of(
            "testApi", ImmutableMap.of(
                "um", ImmutableMap.of(
                    "dois", ImmutableMap.of(
                        "quatro", ImmutableMap.of(
                            "tres", 0,
                            "quatro", 0
                        )
                    )
                )
            ),
            "thisApi", ImmutableMap.of(
                "myMethod", 0
            )
        ), apiMapper
    );

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(apiMapper));


  }


  private void assertMethodsNames(ImmutableMap<String, ?> im, Map<String, ?> check) {
    for (Map.Entry<String, ?> entry : im.entrySet()) {
      assertTrue(check.containsKey(entry.getKey()));
      if (entry.getValue() instanceof ImmutableMap) {
        assertMethodsNames((ImmutableMap<String, ?>) entry.getValue(), (Map<String, ?>) check.get(entry.getKey()));
      }
    }
  }

  private Object[] createProxy() {
    ProxyController proxyController = new ProxyController();
    ClassWithStaticMethod original = new ClassWithStaticMethod();
    InterfaceToProxy proxy = ProxyContainer.createProxy(original, proxyController);
    return new Object[]{
        proxy,
        proxyController
    };
  }

  @Test
  public void loggerInjection() {
    Injector injector = Guice.createInjector(new AProcessorGMTest());
    Logger logger = injector.getInstance(Logger.class);
    assertNotNull(logger);
    logger.log("ola mundo\n");
    logger.getPrintWriter().flush();
  }


  @Test
  public void originalResponse() {
    Object[] proxyArr = createProxy();
    InterfaceToProxy proxy = (InterfaceToProxy) proxyArr[0];
    ProxyController proxyController = (ProxyController) proxyArr[1];

    proxyController.whenCallGetOriginalResponse("testProxy");

    assertEquals("test proxy", proxy.testProxy());
  }


  @Test
  public void alteredReponse() {
    Object[] proxyArr = createProxy();
    InterfaceToProxy proxy = (InterfaceToProxy) proxyArr[0];
    ProxyController proxyController = (ProxyController) proxyArr[1];

    proxyController.when("testProxy", "altered response");

    assertEquals("altered response", proxy.testProxy());

  }

  @Test
  public void mapPrint() {
    HashMap<String, Object> testMap = new HashMap<>();
    testMap.put("10", 10);
    testMap.put("20", 20);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    assertNotNull(testMap);
    System.out.println(gson.toJson(testMap));
  }

  @Test
  public void reflectTypeArrayOfClasses() {
    ApiMethodParameters annotation = AnnotatedObject.class.getAnnotation(ApiMethodParameters.class);
    try {
      int[] intArray = {0, 1};

      assertTrue(intArray.getClass().isArray());
      assertTrue(intArray.getClass().getComponentType().isPrimitive());

      assertEquals(2, Array.getLength(intArray));

      Class<?> returnType = ApiMethodParameters.class.getDeclaredMethod("validators").getReturnType();
      assertTrue(returnType.isArray());
      assertEquals(Class.class, returnType.getComponentType());
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void recursiveInjection() {
    Injector injector = Guice.createInjector(new AProcessorGMTest());
    Logger logger = injector.getInstance(Logger.class);
    AnProcWriters writers = injector.getInstance(AnProcWriters.class);
    assertSame(logger, writers.getlogger());
  }

  @Test
  public void generateJsonListMessage() {
    String format = "primeira:{0} ,segunda:{1} terceira:{2}-{3}";
    MessageFormat messageFormat = new MessageFormat(format);

    String[] formats = new String[messageFormat.getFormats().length];
    for (int i = 0; i < formats.length; i++) {
      formats[i] = "','";
    }
    String msgList = "['" + messageFormat.format(formats) + "']";

    Assert.assertEquals("['primeira:',' ,segunda:',' terceira:','-','']", msgList);
  }

}
