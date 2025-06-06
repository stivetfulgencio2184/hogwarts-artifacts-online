package org.alpha.omega.hogwarts_artifacts_online.configuration;

import org.alpha.omega.hogwarts_artifacts_online.common.utility.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1, 1);
    }
}
