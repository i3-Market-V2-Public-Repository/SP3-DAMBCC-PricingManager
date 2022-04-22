package com.gft.microservices;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gft.i3market.I3MarketApplication;

@AutoConfigureMockMvc
@SpringBootTest(classes = I3MarketApplication.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.properties") // OVERRIDE ENVIROMENT
public class CostControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	@Test
	@Order(500) 
	public void getPriceResponse() throws Exception {
		/*
		 * Test list:
		 *  - get ok responde
		 *  - get cost 0 by price 0
		 *  - get bad request passing dummy argument instead of price
		 *  - set 10% fee
		 *  - get cost of 100*fee = 110
		 *  - set 20% fee
		 *  - get cost of 100*fee = 120
		 */
		
		// Check correct test enviroment. Property file defined in application.test.properties 
		//is different between enviroments
		String eniviroment =env.getActiveProfiles()[0];
		eniviroment.equals(eniviroment);
		
		// Peform base request considering only the status of response
		mvc.perform(MockMvcRequestBuilders.get("/cost/getcost?price=100").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// Perform base test assuming that formula is cost = price * fee, so if price is 0 cost is always 0
		mvc.perform(MockMvcRequestBuilders.get("/cost/getcost?price=0").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("0.0")));
		
		// Perform test using an invalid query param
		mvc.perform(MockMvcRequestBuilders.get("/cost/getcost?dummyparam=1").accept(MediaType.ALL))
			.andExpect(status().isBadRequest());
		
		// Set fee as 10
		mvc.perform(MockMvcRequestBuilders.put("/cost/setfee?fee=10").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("OK")));
		
		// Get cost with a fee of 10% so cost = 100 * 10% = 10
		mvc.perform(MockMvcRequestBuilders.get("/cost/getcost?price=100").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("10.0")));
		
		// Set fee as 20
		mvc.perform(MockMvcRequestBuilders.put("/cost/setfee?fee=20").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("OK")));
		
		// Get cost with a fee of 20% so cost = 100 * 20% = 20
		mvc.perform(MockMvcRequestBuilders.get("/cost/getcost?price=100").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("20.0")));
	}
}