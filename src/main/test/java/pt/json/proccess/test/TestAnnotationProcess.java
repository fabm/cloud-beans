package pt.json.proccess.test;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import pt.gapiap.cloud.maps.ApiMapper;
import pt.gapiap.cloud.maps.ApiMethod;
import pt.gapiap.convert.DeclaredTypeCv;
import pt.gapiap.convert.TypeElementCv;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.DefaultValidator;
import pt.json.proccess.test.apiMap.inject.AProcessorGMTest;
import pt.json.proccess.test.examples.AnnotatedObject;
import pt.json.proccess.test.examples.ClassWithStaticMethod;
import pt.json.proccess.test.examples.InterfaceToProxy;
import pt.json.proccess.test.examples.OtherAnnotatedObject;
import pt.json.proccess.test.guice.AnotherModuleTest;
import pt.json.proccess.test.guice.ModuleTest;
import pt.json.proccess.test.guice.MyModule;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.*;

public class TestAnnotationProcess {

    private static TypeElement createTypeElement(Class<?> clazz) {
        return new TypeElementCv(clazz);
    }

    private static DeclaredType createDeclaredType(Class<?> clazz) {
        return new DeclaredTypeCv(clazz);
    }

    private static Map<String, Object> getValuesMap(TypeElement typeElement) {
        Map<String, Object> returnMap = new HashMap<>();
        for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors()) {
            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> map = annotationMirror.getElementValues().entrySet();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : map) {
                Object value;
                try {
                    value = entry.getValue().getValue();
                } catch (MirroredTypeException e) {
                    value = e.getTypeMirror();
                }
                switch (entry.getKey().getSimpleName().toString()) {
                    case "api":
                        returnMap.put("api", value);
                        break;
                    case "method":
                        returnMap.put("method", value);
                        break;
                    case "validator":
                        returnMap.put("validator", value);
                }
            }
        }
        return returnMap;
    }

    @Test
    public void uniqueTypeElements() {
        Set<DeclaredType> declaredTypes = new HashSet<>();
        Set<TypeElement> typeElements = new HashSet<>();


        declaredTypes.add(createDeclaredType(AnnotatedObject.class));
        declaredTypes.add(createDeclaredType(OtherAnnotatedObject.class));

        typeElements.add(createTypeElement(AnnotatedObject.class));
        typeElements.add(createTypeElement(OtherAnnotatedObject.class));

        Assert.assertEquals(2, declaredTypes.size());
        Assert.assertEquals(2, typeElements.size());

        declaredTypes.add(createDeclaredType(AnnotatedObject.class));
        declaredTypes.add(createDeclaredType(OtherAnnotatedObject.class));

        typeElements.add(createTypeElement(AnnotatedObject.class));
        typeElements.add(createTypeElement(OtherAnnotatedObject.class));

        Assert.assertEquals(2, declaredTypes.size());
        Assert.assertEquals(2, typeElements.size());
    }

    @Test
    public void printAnnotation() {
        Map<String, Object> map = getValuesMap(createTypeElement(AnnotatedObject.class));
        Assert.assertEquals(createDeclaredType(DefaultValidator.class), map.get("validator"));
        Assert.assertEquals("testApi", map.get("api").toString());
        Assert.assertEquals("um.dois.quatro.tres", map.get("method").toString());
        map = getValuesMap(createTypeElement(OtherAnnotatedObject.class));
        Assert.assertEquals(createDeclaredType(DefaultValidator.class), map.get("validator"));
        Assert.assertEquals("thisApi", map.get("api").toString());
        Assert.assertEquals("myMethod", map.get("method").toString());
    }

    @Test
    public void testGuice() {
        Injector injector = Guice.createInjector(new MyModule());

        ModuleTest moduleTest = injector.getInstance(ModuleTest.class);
        Assert.assertEquals("this class is binded", moduleTest.getBindedClass().toString());
        Assert.assertEquals("class injected 1 times", moduleTest.getBindedSingletoneClass().toString());

        AnotherModuleTest anotherModuleTest = injector.getInstance(AnotherModuleTest.class);
        Assert.assertEquals("class injected 2 times", anotherModuleTest.getBindedSingletoneClass().toString());
    }


    @Test
    public void apiMap() {
        Injector injector = Guice.createInjector(new AProcessorGMTest());

        ApiMapper apiMapper = injector.getInstance(ApiMapper.class);
        apiMapper.init();

        Assert.assertNotNull(apiMapper);
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
    }

    private void assertMethodsNames(ImmutableMap<String, ?> im, Map<String,?> check) {
        for(Map.Entry<String,?> entry:im.entrySet()){
            Assert.assertTrue(check.containsKey(entry.getKey()));
            if(entry.getValue() instanceof ImmutableMap){
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
        Assert.assertNotNull(logger);
        logger.log("ola mundo\n");
        logger.getPrintWriter().flush();
    }


    @Test
    public void originalResponse() {
        Object[] proxyArr = createProxy();
        InterfaceToProxy proxy = (InterfaceToProxy) proxyArr[0];
        ProxyController proxyController = (ProxyController) proxyArr[1];

        proxyController.whenCallGetOriginalResponse("testProxy");

        Assert.assertEquals("test proxy", proxy.testProxy());
    }


    @Test
    public void alteredReponse() {
        Object[] proxyArr = createProxy();
        InterfaceToProxy proxy = (InterfaceToProxy) proxyArr[0];
        ProxyController proxyController = (ProxyController) proxyArr[1];

        proxyController.when("testProxy", "altered response");

        Assert.assertEquals("altered response", proxy.testProxy());

    }

    @Test
    public void mapPrint() {
        HashMap<String, Object> testMap = new HashMap<>();
        testMap.put("10", 10);
        testMap.put("20", 20);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Assert.assertNotNull(testMap);
        System.out.println(gson.toJson(testMap));
    }

}
