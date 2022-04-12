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
@Order(13) 
public class PriceControllerDefaultParameterValue {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	@Test
	@Order(13) 
	public void checkDefaultParameterValue() throws Exception {
				
		// Configure formula using default values:
		// formula=f(p1,c1)=p1+c1
		// p1 required true default value as default (1)
		// c1 value = 1
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulawithdefaultconfiguration?formula=f(p1,c1)=p1+c1&contantslist=c1&parameterslist=p1").accept(MediaType.ALL))
			.andExpect(status().isOk());
		
		// TEST 12
		// Configure formula using JSON:
		// formula=f(p1,p2,c1)=p1+p2+c1
		// p1 required true default value as default (1)
		// p2 required false default value as default (1)
		// c1 value = 1
		String jsonconfig = "{\"formula\":\"f(p1,p2,c1)=p1+p2+c1\",\"formulaConstantConfiguration\":[{\"name\":\"c1\",\"description\":\"default description for constant c1\",\"value\":\"1\"}],\"formulaParameterConfiguration\":[{\"name\":\"p1\",\"description\":\"default description for parameter p1\",\"required\":\"true\",\"defaultvalue\":\"1\"},{\"name\":\"p2\",\"description\":\"default description for parameter p2\",\"required\":\"false\",\"defaultvalue\":\"1\"}]}";
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