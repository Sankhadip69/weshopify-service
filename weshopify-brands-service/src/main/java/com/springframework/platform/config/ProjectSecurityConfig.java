package com.springframework.platform.config;

import com.springframework.platform.exception.FilterChainExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@Slf4j
public class ProjectSecurityConfig {

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationService)
                        ,BasicAuthenticationFilter.class)
                .addFilterAfter(filterChainExceptionHandler, LogoutFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("ignoring the security");
        return (web) -> web.ignoring()
                .requestMatchers("/swagger-ui.html","/swagger-ui/***","/v3/api-docs/**");
    }
}
