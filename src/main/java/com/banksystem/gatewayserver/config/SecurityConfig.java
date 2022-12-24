package com.banksystem.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain  securityWebFilterChain(ServerHttpSecurity http){
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/banksystem/accounts/**").authenticated()
                .pathMatchers("/banksystem/cards/**").authenticated()
                .pathMatchers("/banksystem/loans/**").authenticated())
                .oauth2Login(Customizer.withDefaults());
        http.csrf().disable();
        return http.build();



    }


}
