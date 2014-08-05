package pt.gapiap.convert;

public interface Conversor<O,C> {
    void setOriginal(O original);
    C getConverted();
}
