package nextstep.worker;


public interface AsyncEventHandler<T extends ApplicationEvent> {
    void handleAsync(T event);
}
