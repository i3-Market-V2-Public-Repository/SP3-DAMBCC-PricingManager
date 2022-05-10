package com.gft.i3market.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Value("${server.servlet.context-path}")
	private String contextpath;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url(contextpath))
				.components(new Components())
				.info(new Info()
						.title("Price Manager API")
						.description(
								"Pricing Manager is a microservice for configuring and evaluating the price and the fee of data in the I3M environment")
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0.html"))
						.version("1.0.0"));
	}
}

