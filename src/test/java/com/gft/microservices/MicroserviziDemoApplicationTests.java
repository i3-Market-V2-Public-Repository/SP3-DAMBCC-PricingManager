package com.gft.microservices;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gft.i3market.I3MarketApplication;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@AutoConfigureMockMvc
@SpringBootTest(classes = I3MarketApplication.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.properties") // OVERRIDE ENVIROMENT
@TestMethodOrder(OrderAnnotation.class)
class MicroserviziDemoApplicationTests {

	@Test
	void contextLoads() {
		// Documentation
		//https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles

	}

}
