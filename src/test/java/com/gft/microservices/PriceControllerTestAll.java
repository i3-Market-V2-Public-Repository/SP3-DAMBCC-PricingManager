package com.gft.microservices;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@SpringBootTest(classes = {I3MarketApplication.class,CustomOrder.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.properties") // OVERRIDE ENVIROMENT
@TestMethodOrder(OrderAnnotation.class)
@Order(100) 
public class PriceControllerTestAll {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	
	@Test
	@Order(100) 
	public void getPriceResponse() throws Exception {
		/*
		 * Test list:
		 *  - configure formula f(p1,p2,c1)=p1*p2+c1 with default params and constants configuration and expect ok
		 *  - get json configuration
		 *  - configure formula,parameters and constants using Jquery. formula is f(p1,p2,c1)=p1*p2+c1 with c1 = 1 and expect ok
		 *  - eval p1*p2+c1 with p1=1 and p2=1
		 *  - configure formula,parameters and constants using Jquery. formula is f(p1,p2,c1)=p1+p2+c1 with c1 = 1 and expect ok
		 *  - eval p1*p2+c1 with p1=1 and p2=2
		 *  - check bad request pasing an invalid dummy argument
		 *  - check formula congruence
		 *  - configure formula f(p1,c1)=p1+c1 with default params and constants configuration and expect ok
		 *  - put c1 constant with value of 3 and expect ok
		 *  - eval p1+c1 with p1=1 (c1 was configured before with value of 3)
		 *  - configure formula,parameters and constants using Jquery. formula is f(p1,p2,c1)=p1+p2+c1 with c1 = 1 and p2 not required with default value of 1 and expect ok
		 *  - eval p1+p2+c1 with p1=1 without pass p2 parameter check that default is used correctly
		 */
	
		
		String jsonconfig="";
		// Check correct test enviroment. Property file defined in application.test.properties 
		//is different between enviroments
		String eniviroment =env.getActiveProfiles()[0];
		eniviroment.equals(eniviroment);

		// Test 1
		// Configure formula using default values:
		// formula=f(p1,p2,c1)=p1*p2+c1
		// p1 required true default value as default (1)
		// p2 required true default value as default (1)
		// c1 value = 1
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulawithdefaultconfiguration?formula=f(p1,p2,c1)=p1*p2+c1&contantslist=c1&parameterslist=p1,p2").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// Test 2
		// Get json configuration
		mvc.perform(MockMvcRequestBuilders.get("/price/getformulajsonconfiguration").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// Test 3
		// Configure formula using JSON:
		// formula=f(p1,p2,c1)=p1*p2+c1
		// p1 required true default value as default (1)
		// p2 required true default value as default (1)
		// c1 value = 1
		jsonconfig = "{\"formula\":\"f(p1,p2,c1)=p1*p2+c1\",\"formulaConstantConfiguration\":[{\"name\":\"c1\",\"description\":\"default description for constant c1\",\"value\":\"1\"}],\"formulaParameterConfiguration\":[{\"name\":\"p1\",\"description\":\"default description for parameter p1\",\"required\":\"true\",\"defaultvalue\":\"1\"},{\"name\":\"p2\",\"description\":\"default description for parameter p2\",\"required\":\"true\",\"defaultvalue\":\"1\"}]}";
		jsonconfig = URLEncoder.encode(jsonconfig, StandardCharsets.US_ASCII);
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulajsonconfiguration?jsonConfiguration="+jsonconfig).accept(MediaType.ALL))
	    	.andExpect(status().isOk());
		
		// Test 4
		// Perform evaluation test
		// p1*p2+c1
		// p1 = 1
		// p2 = 2
		// c1 = 1
		// f = 1*2+1 = 3
		mvc.perform(MockMvcRequestBuilders.get("/price/getprice?p1=1&p2=2").accept(MediaType.ALL))
			.andExpect(status().isOk())
		    .andExpect(content().string(equalTo("3.0")));
				
		// Test 5
		// Configure formula using JSON:
		// formula=f(p1,p2,c1)=p1+p2+c1
		// p1 required true default value as default (1)
		// p2 required true default value as default (1)
		// c1 value = 2
		jsonconfig = "{\"formula\":\"f(p1,p2,c1)=p1+p2+c1\",\"formulaConstantConfiguration\":[{\"name\":\"c1\",\"description\":\"default description for constant c1\",\"value\":\"2\"}],\"formulaParameterConfiguration\":[{\"name\":\"p1\",\"description\":\"default description for parameter p1\",\"required\":\"true\",\"defaultvalue\":\"1\"},{\"name\":\"p2\",\"description\":\"default description for parameter p2\",\"required\":\"true\",\"defaultvalue\":\"1\"}]}";
		jsonconfig = URLEncoder.encode(jsonconfig, StandardCharsets.US_ASCII);
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulajsonconfiguration?jsonConfiguration="+jsonconfig).accept(MediaType.ALL))
	    	.andExpect(status().isOk());
		
		// Test 6
		// Perform evaluation test
		// p1+p2+c1
		// p1 = 1
		// p2 = 2
		// c1 = 2
		// f = 1+2+2 = 5
		mvc.perform(MockMvcRequestBuilders.get("/price/getprice?p1=1&p2=2").accept(MediaType.ALL))
			.andExpect(status().isOk())
		    .andExpect(content().string(equalTo("5.0")));	
			
		
		// TEST 7
		// Test passing invalid argument (espect bad request)
		mvc.perform(MockMvcRequestBuilders.get("/price/getprice?dummyparam=1").accept(MediaType.ALL))
			.andExpect(status().isBadRequest());
		
		// TEST 8
		// Test formula congruence
		mvc.perform(MockMvcRequestBuilders.get("/price/checkformulaconfiguration").accept(MediaType.ALL))
		 	.andExpect(content().string(equalTo("OK")));	
		
		// TEST 9
		// Configure formula using default values:
		// formula=f(p1,c1)=p1+c1
		// p1 required true default value as default (1)
		// c1 value = 1
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulawithdefaultconfiguration?formula=f(p1,c1)=p1+c1&contantslist=c1&parameterslist=p1").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// TEST 10
		// Change the value of constant
		// c1 = 3
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulaconstant?name=c1&description=test&value=3").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// TEST 11
		// Perform evaluation test
		// p1+c1
		// p1 = 1
		// c1 = 3
		// f = 1+3
		mvc.perform(MockMvcRequestBuilders.get("/price/getprice?p1=1").accept(MediaType.ALL))
			.andExpect(status().isOk())
		    .andExpect(content().string(equalTo("4.0")));		
		
		// TEST 12
		// Configure formula using JSON:
		// formula=f(p1,p2,c1)=p1+p2+c1
		// p1 required true default value as default (1)
		// p2 required false default value as default (1)
		// c1 value = 1
		jsonconfig = "{\"formula\":\"f(p1,p2,c1)=p1+p2+c1\",\"formulaConstantConfiguration\":[{\"name\":\"c1\",\"description\":\"default description for constant c1\",\"value\":\"1\"}],\"formulaParameterConfiguration\":[{\"name\":\"p1\",\"description\":\"default description for parameter p1\",\"required\":\"true\",\"defaultvalue\":\"1\"},{\"name\":\"p2\",\"description\":\"default description for parameter p2\",\"required\":\"false\",\"defaultvalue\":\"1\"}]}";
		jsonconfig = URLEncoder.encode(jsonconfig, StandardCharsets.US_ASCII);
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulajsonconfiguration?jsonConfiguration="+jsonconfig).accept(MediaType.ALL))
	    	.andExpect(status().isOk());

		// TEST 13
		// Perform evaluation test using default parameter value
		// p1+p2+c1
		// p1 = 1
		// p2 = 1 AS DEFAIULT
		// c1 = 1
		// f = 1+1+1 = 3
		mvc.perform(MockMvcRequestBuilders.get("/price/getprice?p1=1").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("3.0")));		
	}	
}