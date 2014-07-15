package pt.gapiap.proccess;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pt.gapiap.proccess.exceptions.InvalidValidator;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.ValidationMethod;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.wrappers.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SupportedAnnotationTypes({
        "pt.gapiap.proccess.annotations.ApiMethodParameters",
        "pt.gapiap.proccess.validation.ValidationMethod"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

    private static PrintWriter out;
    public Bundle aliasBundle = new Bundle("alias");

    private static String toUtf8(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("ISO-8859-1"), "UTF-8");
    }

    private static String toLatin1(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("UTF-8"), "ISO-8859-1");
    }

    private static AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationClass) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (isMirrorOf(annotationMirror, annotationClass)) {
                return annotationMirror;
            }
        }
        return null;
    }

    private static boolean isTypeOf(TypeElement type, Class<?> clazz) {
        if (type.getQualifiedName().contentEquals(clazz.getName())) {
            return true;
        }
        return false;
    }

    private static boolean isMirrorOf(AnnotationMirror annotationMirror, Class<? extends Annotation> clazz) {
        if (clazz.getName().equals(annotationMirror.getAnnotationType().toString())) {
            return true;
        }
        return false;
    }

    private void printValidations() {

    }

    private void note(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, s);
    }

    private void error(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, s);
    }

    private void createLogger() throws IOException {
        FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "compile.log");
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
                f.openOutputStream(), StandardCharsets.UTF_8), true);
        Logger.create(printWriter);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            createLogger();
            if (out == null) {
                FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "x.txt");
                note("gravado em :" + f.toUri().getPath());
                out = new PrintWriter(new OutputStreamWriter(
                        f.openOutputStream(), StandardCharsets.UTF_8), true);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            out.append(toUtf8(gson.toJson(aliasMap())) + "\n");

            Map<String, ?> mapApi = null;
            try {
                mapApi = getMapApi(env, annotations);
                Logger.get().log(mapApi);
            } catch (InvalidElementException e) {
                Logger.get().log(e);
            }


            if (mapApi != null) {
                out.append(toUtf8(gson.toJson(mapApi)) + "\n");
            }

        } catch (IOException e) {
            note(String.format("Nao foi possivel escrever no ficheiro %s", e.getMessage()));
        } catch (InvalidValidator invalidValidator) {
            error(invalidValidator.getMessage());
        } catch (RuntimeException re) {
            Logger.get().log(re);
        }
        out.close();
        Logger.get().close();
        return false;
    }

    private Map<String, ?> getMapApi(RoundEnvironment env, Set<? extends TypeElement> annotations) throws InvalidValidator, InvalidElementException {
        Map api = new HashMap();
        for (TypeElement te : annotations) {
            for (Element e : env.getElementsAnnotatedWith(te)) {


                List<AnnotationWrapper> law = AnnotationWrapperFactory.createAnnotations(e);

                for (AnnotationWrapper annotationWrapper : law) {
                    if (annotationWrapper instanceof ApiMethodPMW) {
                        ApiMethodPMW apiMethodPMW = (ApiMethodPMW) annotationWrapper;

                        apiMethodPMW.setSuperTypes(processingEnv.getTypeUtils().directSupertypes(apiMethodPMW.getElement().asType()));

                        Map<String, Object> mapMethod = null;
                        Object objMapMethod = (Map<String, Object>) api.get(apiMethodPMW.getApi());
                        if (objMapMethod == null) {
                            mapMethod = new HashMap<>();
                            api.put(apiMethodPMW.getApi(), mapMethod);
                        } else {
                            mapMethod = (Map<String, Object>) objMapMethod;
                        }
                        Map<String, Object> mapFields = new HashMap<>();
                        for (ApiField apiField : apiMethodPMW.getFields()) {
                            mapFields.put(apiField.getName(), apiField.getValidationRules());
                            splittedMap(mapMethod, apiMethodPMW.getMethod(), mapFields);
                        }
                    }
                }

            }
        }
        return api;
    }

    private Set<Class<? extends Annotation>> getAnnotations(Object validator)
            throws InvalidValidator {
        Set<Class<? extends Annotation>> annotations = new HashSet<>();
        Class<?> clazz = validator.getClass();
        if (validator == null) {
            throw new InvalidValidator("Validador vazio");
        }

        for (Method method : clazz.getMethods()) {
            ValidationMethod vm = method.getAnnotation(ValidationMethod.class);
            if (vm != null) {
                if (!annotations.add(vm.value())) {
                    throw new InvalidValidator("Validação repetida");
                }
            }
        }

        if (annotations.isEmpty()) {
            throw new InvalidValidator("Validador sem validações");
        }
        return null;
    }

    private void testPrint(Class<?> clazz) {
        Elements eu = processingEnv.getElementUtils();
        //hPrinter.print(clazz.getPackage().getName()+"\n");

        /*PackageElement pe = eu.getPackageElement(clazz.getPackage().getName());
        for (TypeElement typeElement : ElementFilter.typesIn(pe.getEnclosedElements())) {
            hPrinter.print(typeElement.toString() + "\n");
        }*/
    }

    private boolean mapElement(Map api, ApiMethodPMW apiMethodPMW) throws InvalidValidator, InvalidElementException {
        Map map;
        Object objectMap;

        if (apiMethodPMW != null) {
            objectMap = api.get(apiMethodPMW.getApi());
            if (objectMap == null) {
                map = new HashMap();
                api.put(apiMethodPMW.getApi(), map);
            } else {
                map = (Map) objectMap;
            }
            objectMap = api.get(apiMethodPMW.getApi());
            if (objectMap != null) {
                error("O nome do método " + apiMethodPMW.getMethod() + " já existe");
                return true;
            }
            HashMap method = new HashMap<>();
            api.put(apiMethodPMW.getMethod(), method);
            apiMethodPMW.getFields();
/*
            List<VariableElement> fields = fieldsIn(apiMethodPMW.getEnclosedElements());
            for (VariableElement field : fields) {
                Map fieldMap = getFieldMap(field);
                if (fieldMap != null) {
                    String fieldName = field.getSimpleName().toString();
                    method.put(fieldName, fieldMap);
                }
            }
*/
        }
        return false;
    }

    private void splittedMap(Map<String,Object> map,String string,Object value){
        String[] splittedKeys = string.split("\\.");
        Map currentMap = map;
        for (int i = 0; i < splittedKeys.length - 1; i++) {
            String splittedKey = splittedKeys[i];
            Map newMap;
            if (!currentMap.containsKey(splittedKey)) {
                newMap = new HashMap();
                currentMap.put(splittedKey, newMap);
            } else {
                newMap = (Map) currentMap.get(splittedKey);
            }
            currentMap = newMap;
        }
        currentMap.put(splittedKeys[splittedKeys.length-1],value);
    }

    private Map getFieldMap(VariableElement field) {
        HashMap fieldMap = null;
        for (AnnotationMirror annotationMirror : field.getAnnotationMirrors()) {

            isMirrorOf(annotationMirror, Email.class);

            if (fieldMap == null) {
                fieldMap = new HashMap();
            }
            fieldMap.put(
                    annotationMirror.toString(),
                    annotationMirror.getElementValues().toString()
            );
        }
        return fieldMap;
    }

    public Map<String, ?> aliasMap() {
        Iterator<String> keys = aliasBundle.keySet().iterator();
        Map map = new HashMap<>();

        while (keys.hasNext()) {
            String key = keys.next();
            String[] splittedKeys = key.split("\\.");
            Map currentMap = map;
            for (int i = 0; i < splittedKeys.length - 1; i++) {
                String splittedKey = splittedKeys[i];
                Map newMap;
                if (!currentMap.containsKey(splittedKey)) {
                    newMap = new HashMap();
                    currentMap.put(splittedKey, newMap);
                } else {
                    newMap = (Map) currentMap.get(splittedKey);
                }
                currentMap = newMap;
            }
            currentMap.put(splittedKeys[splittedKeys.length - 1], aliasBundle.getString(key));
        }
        return map;
    }
}
