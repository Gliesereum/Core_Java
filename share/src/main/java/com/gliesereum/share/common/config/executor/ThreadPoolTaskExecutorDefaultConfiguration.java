package com.gliesereum.share.common.config.executor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-26
 */
@Configuration
public class ThreadPoolTaskExecutorDefaultConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("task-executor-thread");
        executor.initialize();
        return executor;
    }
}
