package rsb.walker.dax_api.shared;

public class Pair<T, V> {

    private T key;
    private V value;

    public Pair(T key, V value){
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
