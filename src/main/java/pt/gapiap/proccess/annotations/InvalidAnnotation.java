package pt.gapiap.proccess.annotations;

public class InvalidAnnotation extends RuntimeException{
    public InvalidAnnotation(String message) {
        super(message);
    }
}
