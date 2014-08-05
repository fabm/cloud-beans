package pt.gapiap.cloud.tests;

public class CastEvaluationException extends Exception {
    Class<?> expected;
    Class<?> obtained;

    public CastEvaluationException(Class<?> classExpected, Class<?> classObtained) {
        super("Class expected:" + classExpected.getCanonicalName() +
                " class obtained: " + classObtained.getCanonicalName());
        expected = classExpected;
        obtained = classObtained;
    }

}
