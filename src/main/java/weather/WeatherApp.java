package weather;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class WeatherApp {
    private static final Logger LOG = Logger.getLogger(WeatherApp.class);

    @Autowired
    private Environment environment;

    @Bean(name = "weatherSiteApiExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        String threadNamePrefix = environment.getProperty("ws.thread.prefix");
        int corePoolSize = Integer.parseInt(environment.getProperty("ws.thread.core-pool"));
        int maxPoolSize = Integer.parseInt(environment.getProperty("ws.thread.max-pool"));
        int queueCapacity = Integer.parseInt(environment.getProperty("ws.queue.capacity"));
        int threadTimeout = Integer.parseInt(environment.getProperty("ws.thread.timeout"));

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