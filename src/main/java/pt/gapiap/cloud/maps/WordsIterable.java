package pt.gapiap.cloud.maps;

import java.util.Iterator;

public class WordsIterable implements Iterable<String>, Iterator<String> {
    public WordsIterable(String complete) {
        this.complete = complete;
        if(complete == null){
            throw new NullPointerException("The string must be not nul");
        }
        if (complete.startsWith(".")){
            throw new IllegalArgumentException("String must star without a '.' at the begining");
        }
        if (complete.endsWith(".")){
            throw new IllegalArgumentException("String must end without a '.'");
        }
    }

    private String complete;
    private String currentString;
    private String remain;
    private int splitter = 0;

    @Override
    public Iterator<String> iterator() {
        remain = complete;
        calculeSplitter();
        return this;
    }

    private void calculeSplitter() {
        splitter = remain.indexOf('.');
    }

    @Override
    public boolean hasNext() {
        return !(splitter == -1 && remain == null);
    }

    @Override
    public String next() {
        if (splitter == -1) {
            String lastString = remain;
            remain = null;
            return lastString;
        }
        currentString = remain.substring(0, splitter);
        if (currentString.isEmpty()){
            throw new IllegalArgumentException("Strings between '.' must be not empty");
        }
        remain = remain.substring(splitter + 1);
        calculeSplitter();
        return currentString;
    }

    @Override
    public void remove() {

    }
}
