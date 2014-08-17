package pt.gapiap.cloud.endpoints.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import pt.gapiap.cloud.endpoints.errors.SimpleError;

import java.lang.reflect.Type;

public class SimpleErrorSerializer extends CEErrorSerializer implements JsonSerializer<SimpleError> {
    @Override
    public JsonElement serialize(SimpleError src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject superSerilized = serialize(src);
        superSerilized.add("vars", context.serialize(src.getVars()));
        return superSerilized;
    }
}
