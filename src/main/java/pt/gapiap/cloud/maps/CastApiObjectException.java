package pt.gapiap.cloud.maps;

public class CastApiObjectException extends RuntimeException{
    Class<? extends ApiObject> expected;
    Class<? extends ApiObject> obtained;

    public CastApiObjectException(Class<? extends ApiObject> expected, ApiObject obtained) {
        super("Expected "+expected.getCanonicalName()+" but obtained "+obtained.getClass().getCanonicalName());
        this.expected = expected;
        this.obtained = obtained.getClass();
    }

    public Class<? extends ApiObject> getExpected() {
        return expected;
    }

    public Class<? extends ApiObject> getObtained() {
        return obtained;
    }
}
