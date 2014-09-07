package pt.gapiap.errors;

public class ErrorManager {

    private int lastIndex = 1;

    void registerRange(ErrorArea errorArea){
        errorArea.setStartIndex(lastIndex);
        lastIndex+= errorArea.getSize();
    }
}
