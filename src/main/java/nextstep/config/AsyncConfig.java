package nextstep.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync(proxyTargetClass = true)
@Configuration
@RequiredArgsConstructor
public class AsyncConfig extends AsyncConfigurerSupport {
    private static final int MINUTE = 60;
    private static final int MAX_CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 20;
    private static final String THREAD_NAME_PREFIX = "PROFIT-ASYNC-";

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(MAX_CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(MINUTE);
        executor.initialize();
        return executor;
    }
}
