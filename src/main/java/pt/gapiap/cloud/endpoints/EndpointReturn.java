package pt.gapiap.cloud.endpoints;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import pt.gapiap.cloud.endpoints.errors.Failure;

import java.util.Map;

public class EndpointReturn {
  private Failure failure;
  private Map<String, String> msgMap;
  @Inject
  private Injector injector;

  /**
   * Map with language/message
   * Ex: key='EN' value = 'User update with success'
   *
   * @param msgMap
   */
  public void setMsgMap(Map<String, String> msgMap) {
    this.msgMap = msgMap;
  }

  public EndpointReturn(Failure failure) {
    this.failure = failure;
  }

  public EndpointReturn() {
  }

  @JsonIgnore
  public Map<String, String> getMsgMap() {
    return msgMap;
  }

  public int getCode() {
    if (failure == null) {
      return 1;
    }
    return failure.getCode();
  }

  public String getMessage() {
    if(msgMap != null){
      String lang = injector.getInstance(Key.get(String.class, Names.named("language")));
      String msg = msgMap.get(lang);
      if(msg == null){
        msg = msgMap.get("en");
      }
      if(msg != null){
        return msg;
      }
    }
    if (failure == null) {
      return "ok";
    }
    return failure.render();
  }

}
