package com.wbt.todo_app.config.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
@Getter
public class JwtConverterProperties {

    private String resourceId;
    private String principalAttribute;

}
