package pt.gapiap.cloud.endpoints.errors;

import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.map.type.MapType;
import com.google.appengine.repackaged.org.codehaus.jackson.type.JavaType;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.http.HttpException;

import javax.servlet.ServletException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class CEError extends RuntimeException {

  private Map<String, ?> error;

  public CEError(Failure failure) {
    init(failure);
  }

  public CEError(Failure... failures) {
    init(Arrays.asList(failures));
  }

  public CEError(Iterable<Failure> failures) {
    init(failures);
  }

  private void init(Iterable<Failure> failures) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Failure failure : failures) {
      //TODO carece de testes de converção de uma string JSON para Gson
    }
  }

  private void init(Failure failure) {
    String message = failure.render();
    int code = failure.getCode();
    Map<String, ? extends Serializable> errorMap = ImmutableMap.of(
        "code", code,
        "message", message
    );
    error = ImmutableMap.of(
        "error", errorMap
    );

  }



  @Override
  public String getMessage() {
    Gson gson = new Gson();
    return gson.toJson(error);
  }

  public Map getMap() {
    return error;
  }

}
