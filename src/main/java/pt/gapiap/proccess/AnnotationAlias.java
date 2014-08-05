package pt.gapiap.proccess;

import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Required;

import java.util.HashMap;
import java.util.Map;

public class AnnotationAlias {
    protected static Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put(Required.class.getCanonicalName(), "Required");
        map.put(Email.class.getCanonicalName(), "Email");
    }

    public String getAlias(String annotationClassName) {
        String alias = map.get(annotationClassName);
        if (alias == null) {
            return annotationClassName;
        }
        return alias;
    }
}
