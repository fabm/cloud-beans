package pt.gapiap.cloud.endpoints.serializers;

import com.google.gson.JsonObject;
import pt.gapiap.cloud.endpoints.errors.CEError;

public class CEErrorSerializer {

    protected JsonObject serialize(CEError src) {
        JsonObject root = new JsonObject();
        JsonObject jsonError = new JsonObject();
        root.add("error", jsonError);
        jsonError.addProperty("code", src.getCeErrorIdentifier().getContext());
        jsonError.addProperty("context", src.getCeErrorIdentifier().getContext());
        return root;
    }
}
