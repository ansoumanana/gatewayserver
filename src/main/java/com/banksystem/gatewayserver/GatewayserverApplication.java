package com.banksystem.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@EnableEurekaClient
public class GatewayserverApplication {

	private final TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory;

	public GatewayserverApplication(TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory) {
		this.tokenRelayGatewayFilterFactory = tokenRelayGatewayFilterFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder ){

		return builder.routes()
				.route(p -> p
						.path("/banksystem/accounts/**")
						.filters(f -> f
								.filter(tokenRelayGatewayFilterFactory.apply())
								.rewritePath("/banksystem/accounts/(?<segment>.*)","/${segment}")
								//.addResponseHeader("X-Response-Time",new Date().toString()))
								//Avoid security issue
								.removeRequestHeader("Cookie"))
						.uri("lb://ACCOUNT")).
				route(p -> p
						.path("/banksystem/loans/**")
						.filters(f -> f
								.filter(tokenRelayGatewayFilterFactory.apply())
								.rewritePath("/banksystem/loans/(?<segment>.*)","/${segment}")
								//.addResponseHeader("X-Response-Time",new Date().toString()))
								.removeRequestHeader("Cookie"))
						.uri("lb://LOANS")).
				route(p -> p
						.path("/banksystem/cards/**")
						.filters(f -> f
								.filter(tokenRelayGatewayFilterFactory.apply())
								.rewritePath("/banksystem/cards/(?<segment>.*)","/${segment}")
								//.addResponseHeader("X-Response-Time",new Date().toString()))
								.removeRequestHeader("Cookie"))
						.uri("lb://CARDS")).build();

	}

}
