package pt.gapiap.cloud.maps;

public interface ApiObject {

    Type getType();
    String getName();

    public enum Type {
        API, METHOD, METHOD_FIELD_MAP, ANNOTATION_MIRROR_VALUE_MAP, FIELD
    }
}
