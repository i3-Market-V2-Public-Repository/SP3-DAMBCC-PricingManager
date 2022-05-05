package com.gft.microservices;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.gft.i3market.I3MarketApplication;

@AutoConfigureMockMvc
@SpringBootTest(classes = I3MarketApplication.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.properties") // OVERRIDE ENVIROMENT
@TestMethodOrder(OrderAnnotation.class)
@Order(5) 
public class PriceControllerTestGetFormulaCongruence {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	@Test
	@Order(8) 
	public void getFormulaCongruence() throws Exception {
				
		String jsonconfig="";
		// Check correct test enviroment. Property file defined in application.test.properties 
		//is different between enviroments
		String eniviroment =env.getActiveProfiles()[0];
		eniviroment.equals(eniviroment);

		// TEST 8
		// Test formula congruence
		mvc.perform(MockMvcRequestBuilders.get("/price/checkformulaconfiguration").accept(MediaType.ALL))
		 	.andExpect(content().string(equalTo("Correct formula")));
	}
}