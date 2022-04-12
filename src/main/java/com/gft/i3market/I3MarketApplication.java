package com.gft.i3market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
@SpringBootApplication
public class I3MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(I3MarketApplication.class, args);
	}
	
}
