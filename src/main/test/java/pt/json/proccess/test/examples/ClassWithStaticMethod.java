package pt.json.proccess.test.examples;

public class ClassWithStaticMethod implements InterfaceToProxy {
    private static String string = "default value";

    public static String testStatic() {
        string = "it's not the default any more";
        return "return of the static method";
    }

    @Override
    public String testProxy() {
        return "test proxy";
    }
}
