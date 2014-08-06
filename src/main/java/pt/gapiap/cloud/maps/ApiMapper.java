package pt.gapiap.cloud.maps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.*;

public class ApiMapper extends Hashtable<String, ApiMethod> {
    private Set<ApiValidator> validatorSet;
    @Inject
    private RoundEnvironment roundEnvironment;
    @Inject
    private Logger logger;
    @Inject
    private Injector injector;


    private ApiValidator getOrCreateValidator(DeclaredType declaredType) {
        for (ApiValidator apiValidator : validatorSet) {
            if (apiValidator.getValidator().equals(declaredType)) {
                return apiValidator;
            }
        }
        ApiValidator apiValidator = injector.getInstance(ApiValidator.class);
        apiValidator.setValidator(declaredType);
        apiValidator.init();
        validatorSet.add(apiValidator);
        return apiValidator;
    }

    private void addMethodsPath(String api, String methodName, ApiValidator apiValidator, TypeElement element) {
        ApiMethod method = get(api);

        WordsIterable wordsIterable = new WordsIterable(methodName);

        boolean newPath = method == null;
        ApiMethod next = null;

        if (newPath) {
            method = injector.getInstance(ApiMethod.class);
            put(api, method);
        }

        for (String word : wordsIterable) {
            boolean isLast = !wordsIterable.hasNext();
            if (isLast) {
                method.setApiValidator(apiValidator);
                method.setTypeElement(element);
                method.resolveMethod(word);
                return;
            }
            if (!newPath) {
                next = (ApiMethod) method.get(word);
                newPath = next == null;
            }
            if (!newPath) {
                method = next;
            } else {
                next = injector.getInstance(ApiMethod.class);
                method.put(word, next);
                method = next;
            }
        }
    }

    public void init() {
        validatorSet = new HashSet<>();
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(ApiMethodParameters.class);
        for (Element element : elementSet) {
            ApiMethodParameters annotation = element.getAnnotation(ApiMethodParameters.class);
            try {
                annotation.validator();
            } catch (MirroredTypeException e) {
                logger.log(annotation.api() + " : " + annotation.method() + "\n");
                logger.log(e.getTypeMirror().toString() + "\n");
                ApiValidator apiValidator = getOrCreateValidator((DeclaredType) e.getTypeMirror());
                //this cast is possible because the target of ApiMethodParameters @Target is ElementType.TYPE
                addMethodsPath(annotation.api(), annotation.method(), apiValidator, (TypeElement) element);
                logger.log(this + "\n");
            }
        }
    }

    public String getJsonPreatifyApisMap() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.toJson(this);
    }

    public String getJsonCompactApisMap() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void printMethods() {
        Iterator<Map.Entry<String, ApiMethod>> es = entrySet().iterator();
        for (Map.Entry<String, ApiMethod> entry : entrySet()) {
            System.out.println(entry.getKey());
            print("    ", entry.getValue());
        }
    }

    private void print(String spc, ApiMethod apiMethod) {
        for (Map.Entry entry : apiMethod.entrySet()) {
            System.out.println(spc + entry.getKey());
            if (entry.getValue() != null && entry.getValue() instanceof ApiMethod) {
                print(spc + "    ", (ApiMethod) entry.getValue());
            }
        }
    }
}
