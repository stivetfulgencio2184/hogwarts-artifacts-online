package org.alpha.omega.hogwarts_artifacts_online.configuration.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "custom-beans")
public class CustomBeansEndpoint {

    private final ApplicationContext applicationContext;

    public CustomBeansEndpoint(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @ReadOperation
    public int beanCount() {
        return this.applicationContext.getBeanDefinitionCount();
    }
}
