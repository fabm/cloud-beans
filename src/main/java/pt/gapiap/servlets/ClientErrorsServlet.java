package pt.gapiap.servlets;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.ErrorLocale;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ClientErrorsServlet extends HttpServlet {

  private Function<Map.Entry<Integer, ErrorLocale>, Map.Entry<Integer, JsonObject>> gsonConvertion = new Function<Map.Entry<Integer, ErrorLocale>,
      Map.Entry<Integer, JsonObject>>() {

    @Nullable
    @Override
    public Map.Entry<Integer, JsonObject> apply(@Nullable Map.Entry<Integer, ErrorLocale> input) {
      ErrorLocale errorLocale = input.getValue();
      Iterator<Map.Entry<String, ErrorTemplate>> it = errorLocale.iterator();
      JsonObject jsonMap = new JsonObject();
      while (it.hasNext()) {
        Map.Entry<String, ErrorTemplate> entry = it.next();
        JsonParser jsonParser = new JsonParser();
        String jsonString = entry.getValue().jsonTemplate();
        JsonElement parsed = jsonParser.parse(jsonString);

        JsonObject jsonIdentifier = new JsonObject();
        jsonIdentifier.add("template", parsed);

        Gson gson = new Gson();
        jsonIdentifier.add("args", gson.toJsonTree(entry.getValue().getJsonArguments()));

        jsonMap.add(entry.getKey(), jsonIdentifier);
      }
      JsonObject wrapper = new JsonObject();
      wrapper.add("lang", jsonMap);
      Gson gson = new Gson();
      wrapper.add("args", gson.toJsonTree(errorLocale.getArgs()));
      return Maps.immutableEntry(input.getKey(), wrapper);
    }
  };
  @Inject
  private ErrorManager errorManager;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Iterable<Map.Entry<Integer, JsonObject>> transformed = Iterables.transform(errorManager.getClientErrors().entrySet(), gsonConvertion);

    JsonObject clientErrors = new JsonObject();

    Gson gson = new Gson();
    for (Map.Entry<Integer, JsonObject> entry : transformed) {
      clientErrors.add(entry.getKey() + "", entry.getValue());
    }

    resp.setContentType("application/json; charset=UTF-8");
    resp.addHeader("Cache-Control", "public, max-age=90000");
    JsonWriter jsonWriter = new JsonWriter(resp.getWriter());
    gson.toJson(clientErrors, jsonWriter);
  }

}
