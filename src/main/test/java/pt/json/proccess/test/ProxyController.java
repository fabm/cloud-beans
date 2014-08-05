package pt.json.proccess.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProxyController {
    private HashMap<String, Object> responses;
    private Set<String> trueCall;

    public ProxyController() {
        responses = new HashMap<>();
        trueCall = new HashSet<>();
    }

    public void when(String request, Object response) {
        responses.put(request, response);
    }

    public void whenCallGetOriginalResponse(String request) {
        trueCall.add(request);
    }

    boolean isTrueCall(String request) {
        for (String current : trueCall) {
            if (request.hashCode() == current.hashCode() && request.equals(current)) {
                return true;
            }
        }
        return false;
    }

    Object getResponse(String request) {
        return responses.get(request);
    }

}
