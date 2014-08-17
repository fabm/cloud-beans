package pt.gapiap.proccess.validation.bean.checker.json;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CheckResultJsonReturn {
    private Map<String, ValidationsJsonReturn> stringValidationsJsonReturnMap;

    public CheckResultJsonReturn() {
        stringValidationsJsonReturnMap = new HashMap<>();
    }

    void putValidation(String fieldName, Annotation annotation){

    }
}
