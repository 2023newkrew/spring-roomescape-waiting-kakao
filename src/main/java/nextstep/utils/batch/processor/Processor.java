package nextstep.utils.batch.processor;

@FunctionalInterface
public interface Processor<T, V> {

    V process(T item);

}
