package com.example.petstore.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource(value = "classpath:application-${environment:dev}.properties")
public class Environment {

    @Value("${default.api.response.time.ms}")
    private long defaultApiResponseTime;
}
