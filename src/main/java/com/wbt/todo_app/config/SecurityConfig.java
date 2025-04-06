package com.wbt.todo_app.config;

import com.wbt.todo_app.config.jwt.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry ->
                        registry.requestMatchers(HttpMethod.GET, "/api/v1/todos/home").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/todos").hasRole("user")
                                .requestMatchers(HttpMethod.POST, "/api/v1/todos/**").hasRole("admin")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/todos/**").hasRole("admin")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/todos/**").hasRole("admin")
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(this.jwtConverter)));

        return http.build();
    }

}
