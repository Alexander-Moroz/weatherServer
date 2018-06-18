package weather;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@SpringBootApplication
public class WeatherApp {
    private static final Logger LOG = Logger.getLogger(WeatherApp.class);

    @Bean(name = "weatherSiteApiExecutor")
    public TaskExecutor threadPoolTaskExecutor(@Value("${ws.thread.prefix}") String threadNamePrefix,
                                               @Value("${ws.thread.core-pool}") int corePoolSize,
                                               @Value("${ws.thread.max-pool}") int maxPoolSize,
                                               @Value("${ws.queue.capacity}") int queueCapacity,
                                               @Value("${ws.thread.timeout}") int threadTimeout) {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);
        threadPoolTaskExecutor.afterPropertiesSet();

        LOG.debug("ThreadPoolTaskExecutor set");
        LOG.debug("Thread name prefix: " + threadNamePrefix);
        LOG.debug("core pool size: " + corePoolSize);
        LOG.debug("max pool size: " + maxPoolSize);
        LOG.debug("queue capacity: " + queueCapacity);
        LOG.debug("Thread timeout: " + threadTimeout + "(sec)");

        return threadPoolTaskExecutor;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApp.class, args);
    }
}