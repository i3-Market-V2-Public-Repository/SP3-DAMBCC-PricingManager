package com.gft.i3market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class I3MarketApplication {

	// https://gitlab.com/i3-market/code/wp3/t3.3/pricing-manager
	public static void main(String[] args) {
		SpringApplication.run(I3MarketApplication.class, args);
	}
	
}
