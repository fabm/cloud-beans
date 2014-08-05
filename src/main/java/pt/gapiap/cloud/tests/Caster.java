package pt.gapiap.cloud.tests;

public class Caster {
    @SuppressWarnings("unchecked")
    public static <T> T castTo(Class<T> clazz, Object object, StringBuilderWithIdentation sb) throws CastEvaluationException {
        if (object.getClass().isAssignableFrom(clazz)) {
            T typeCasted = (T) object;
            sb.addLineF("%-20s:%s", "Sucessfully casted", clazz.getCanonicalName());
            return typeCasted;
        }else{
            throw new CastEvaluationException(clazz,object.getClass());
        }
    }
}
