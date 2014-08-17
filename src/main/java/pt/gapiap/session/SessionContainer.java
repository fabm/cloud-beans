package pt.gapiap.session;

public interface SessionContainer<K> {
    <V> V getValue(K key);
}
