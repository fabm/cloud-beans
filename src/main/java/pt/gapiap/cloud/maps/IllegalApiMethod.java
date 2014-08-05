package pt.gapiap.cloud.maps;

public class IllegalApiMethod extends RuntimeException{
    public IllegalApiMethod(String method) {
        super(
                "The method "+method+" is more than one times "
        );
    }
}
