package pt.gapiap.proccess;

import java.util.ResourceBundle;
import java.util.Set;

public class Bundle {
    private ResourceBundle resourceBundle;

    public Bundle(String id) {
        resourceBundle = ResourceBundle.getBundle(id);
    }


    public String getString(String key) {
        return resourceBundle.getString(key);
    }

    public Set<String> keySet() {
        return resourceBundle.keySet();
    }
}