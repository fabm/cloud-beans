package pt.gapiap.proccess;

import com.google.inject.Guice;
import com.google.inject.Injector;
import pt.gapiap.cloud.maps.ApiMapper;
import pt.gapiap.proccess.exceptions.InvalidValidator;
import pt.gapiap.proccess.inject.AProcessorGM;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.wrappers.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@SupportedAnnotationTypes({
        "pt.gapiap.proccess.annotations.ApiMethodParameters"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {
    private static PrintWriter out;
    private static AProcessorGM aProcessorGM;
    public Bundle aliasBundle = new Bundle("alias");

    private static String toUtf8(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("ISO-8859-1"), "UTF-8");
    }

    private static String toLatin1(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("UTF-8"), "ISO-8859-1");
    }

    private AProcessorGM getaProcessorGM(RoundEnvironment env) {
        if (aProcessorGM == null) {
            aProcessorGM = new AProcessorGM();
            aProcessorGM.setProcessingEnv(processingEnv);
        }
        aProcessorGM.setRoundEnvironment(env);
        return aProcessorGM;
    }

    private void note(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, s);
    }

    private void error(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, s);
    }

    private void createOut() throws IOException {
        if (out == null) {
            FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "validationMap.json");
            note("gravado em :" + f.toUri().getPath());
            out = new PrintWriter(f.openWriter());
        }
    }

    private FileObject compileFo() throws IOException {
        return processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "compile.log");
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (annotations.size() > 0) {
            try {
                process(env);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void process(RoundEnvironment env) {
        AProcessorGM aProcessorGM = new AProcessorGM();
        aProcessorGM.setProcessingEnv(processingEnv);
        aProcessorGM.setRoundEnvironment(env);

        Injector injector = Guice.createInjector(aProcessorGM);

        Logger logger = injector.getInstance(Logger.class);
        ApiMapper apiMapper = injector.getInstance(ApiMapper.class);
        apiMapper.init();
        logger.close();


        try {
            createOut();
            out.print(apiMapper.getJsonPreatifyApisMap());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMap() throws UnsupportedEncodingException {
        ApiMapper apiMapper = new ApiMapper();
        if (apiMapper != null) {
            out.append(apiMapper.toString());
            //out.append(toUtf8(apiMapper.getJsonPreatifyApisMap()));
            //out.append(toUtf8(gson.toJson(mapApi)) + "\n");
        }
    }

    private void test() throws InvalidElementException, InvalidValidator {

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

                        Map<String, Object> mapMethod;
                        Object objMapMethod = api.get(apiMethodPMW.getApi());
                        if (objMapMethod == null) {
                            mapMethod = new HashMap<>();
                            api.put(apiMethodPMW.getApi(), mapMethod);
                        } else {
                            mapMethod = (Map<String, Object>) objMapMethod;
                        }
                        Map<String, Object> mapFields = new HashMap<>();
                        for (ApiField apiField : apiMethodPMW.getFields()) {
                            Map<String, Object> annotationsMap = new HashMap<>();
                            for (ValidationRule validationRule : apiField.getValidationRuleIterable()) {
                                Map<String, Object> attributesMap = new HashMap<>();
                                for (AnnotationMirrorAtribute annotationMirrorAtribute : validationRule.getAnnotationMirrorAttributes()) {
                                    attributesMap.put(annotationMirrorAtribute.getKey(), annotationMirrorAtribute.getValue());
                                }
                                annotationsMap.put(validationRule.getAnnotationMirrorName(), attributesMap);
                            }
                            mapFields.put(apiField.getName(), annotationsMap);
                            splittedMap(mapMethod, apiMethodPMW.getMethod(), mapFields);
                        }
                    }
                }

            }
        }
        return api;
    }


    private void splittedMap(Map<String, Object> map, String string, Object value) {
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
        currentMap.put(splittedKeys[splittedKeys.length - 1], value);
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
