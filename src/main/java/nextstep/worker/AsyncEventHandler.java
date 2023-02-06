package nextstep.worker;


import org.springframework.scheduling.annotation.Async;

public interface AsyncEventHandler<T extends ApplicationEvent> {
    @Async
    void handleAsync(T event);
}
