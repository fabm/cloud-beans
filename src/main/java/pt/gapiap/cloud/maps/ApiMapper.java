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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

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

    private ApiMethod getOrPutApiMap(String api, String methodName, ApiValidator apiValidator) {
        ApiMethod method = get(api);

        WordsIterable wordsIterable = new WordsIterable(methodName);

        String word = null;
        wordsIterable.iterator();
        while (wordsIterable.hasNext()) {
            word = wordsIterable.next();
            ApiMethod next;
            if (method == null) {
                logger.log("new api->" + api + "->first method->" + word + "\n");
                method = injector.getInstance(ApiMethod.class);
                method.setName(word);
                put(api, method);
                next = injector.getInstance(ApiMethod.class);
                method.put(word,next);
                method = next;
            }
            next = (ApiMethod) method.get(word);
            logger.log("exists->" + word + " method?" + (next != null) + "\n");
            if (next == null) {
                break;
            }
            logger.log("pass to next method->" + word + "\n");
            method = next;
        }
        logger.log("from now on needs to construct new path\n");
        while (wordsIterable.hasNext()){
            word=wordsIterable.next();
            ApiMethod newMethod = injector.getInstance(ApiMethod.class);
            newMethod.setName(word);
            method.put(word, newMethod);
            method = newMethod;
        }
        method.setApiValidator(apiValidator);
        return method;
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
                ApiMethod apiMethod = getOrPutApiMap(annotation.api(), annotation.method(), apiValidator);
                //this cast is possible because the target of ApiMethodParameters @Target is ElementType.TYPE
                apiMethod.setTypeElement((TypeElement) element);
                apiMethod.resolveMethod();
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
}
