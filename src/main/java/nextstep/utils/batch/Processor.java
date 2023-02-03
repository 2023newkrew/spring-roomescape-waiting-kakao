package nextstep.utils.batch;

@FunctionalInterface
public interface Processor<T, V> {

    V process(T item);

}
