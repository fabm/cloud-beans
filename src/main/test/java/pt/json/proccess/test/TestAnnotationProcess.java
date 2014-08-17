package pt.json.proccess.test;

import com.google.common.base.Defaults;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.mockito.Mockito;
import pt.gapiap.cloud.maps.ApiMapper;
import pt.gapiap.convert.DeclaredTypeCv;
import pt.gapiap.convert.TypeElementCv;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.json.writer.AnProcWriters;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.bean.checker.proxy.mark.AnnotationProxyMark;
import pt.gapiap.proccess.validation.bean.checker.proxy.mark.AnnotationProxyMarkWOriginal;
import pt.gapiap.proccess.validation.defaultValidator.DefaultValidator;
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
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void printMethodsCalled() {
        Size size = mock(Size.class);
        when(size.max()).thenReturn(6);
        when(size.min()).thenReturn(2);

        when(size.annotationType()).thenReturn((Class)Size.class);

        AnnotationProxyMark<Size> apmSize = new AnnotationProxyMarkWOriginal<>(size,new HashMap<String,Object>());

        Size apmSizeProxy = apmSize.getProxy();
        assertEquals(2, apmSizeProxy.min());
        assertEquals(6, apmSizeProxy.max());

        assertEquals(2, apmSize.valuesMap().get("min"));
        assertEquals(6, apmSize.valuesMap().get("max"));


        AnnotationTest annotationTest = mock(AnnotationTest.class);
        when(annotationTest.classTest()).thenReturn(null);
        when(annotationTest.testPrimitiveInt()).thenReturn(2);


        when(annotationTest.annotationType()).thenReturn((Class) AnnotationTest.class);
        AnnotationProxyMark<AnnotationTest> apmTest = new AnnotationProxyMarkWOriginal<>(annotationTest, new HashMap<String,Object>());
        AnnotationTest apmTestProxy = apmTest.getProxy();

        assertNull(apmTestProxy.classTest());
        assertEquals(2, apmTestProxy.testPrimitiveInt());

        assertNull(apmTest.valuesMap().get("classTest"));
        assertEquals(2, apmTest.valuesMap().get("testPrimitiveInt"));

    }

}
