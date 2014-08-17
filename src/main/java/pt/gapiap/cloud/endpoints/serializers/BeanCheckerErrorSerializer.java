package pt.gapiap.cloud.endpoints.serializers;

import com.google.gson.*;
import pt.gapiap.cloud.endpoints.errors.BeanCheckerError;
import pt.gapiap.cloud.endpoints.errors.Failure;
import pt.gapiap.cloud.endpoints.errors.FieldFailed;
import pt.gapiap.cloud.endpoints.errors.SimpleError;

import java.lang.reflect.Type;

public class BeanCheckerErrorSerializer extends CEErrorSerializer implements JsonSerializer<BeanCheckerError> {

    @Override
    public JsonElement serialize(BeanCheckerError src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject superSerilized = serialize(src);
        JsonArray jsonArrayFieldsFailed = new JsonArray();

        for(FieldFailed fieldFailed:src.getFieldsFailed()){
            JsonObject jsonFieldFailed = new JsonObject();
            jsonFieldFailed.addProperty("field",fieldFailed.getField());
            jsonFieldFailed.add("failList", failList(fieldFailed,context));
            jsonArrayFieldsFailed.add(jsonFieldFailed);
        }

        superSerilized.add("failures",jsonArrayFieldsFailed);

        return superSerilized;
    }

    private JsonArray failList(FieldFailed fieldFailed, JsonSerializationContext context) {
        JsonArray jsonFailures = new JsonArray();
        for(Failure failure:fieldFailed.getFailureList()){
            JsonObject jsonFailure = new JsonObject();
            jsonFailure.addProperty("validation",failure.getName());
            jsonFailure.add("vars",context.serialize(failure.getVars()));
            jsonFailures.add(jsonFailure);
        }
        return jsonFailures;
    }
}
