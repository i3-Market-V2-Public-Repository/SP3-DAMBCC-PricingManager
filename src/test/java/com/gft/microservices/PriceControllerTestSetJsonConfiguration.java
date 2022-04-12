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
@Order(3) 
public class PriceControllerTestSetJsonConfiguration {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	@Test
	@Order(3) 
	public void setFormulaJsoncCnfiguration1() throws Exception {
				
		String jsonconfig="";
		// Check correct test enviroment. Property file defined in application.test.properties 
		//is different between enviroments
		String eniviroment =env.getActiveProfiles()[0];
		eniviroment.equals(eniviroment);

		// Configure formula using JSON:
		// formula=f(p1,p2,c1)=p1*p2+c1
		// p1 required true default value as default (1)
		// p2 required true default value as default (1)
		// c1 value = 1
		jsonconfig = "{\"formula\":\"f(p1,p2,c1)=p1*p2+c1\",\"formulaConstantConfiguration\":[{\"name\":\"c1\",\"description\":\"default description for constant c1\",\"value\":\"1\"}],\"formulaParameterConfiguration\":[{\"name\":\"p1\",\"description\":\"default description for parameter p1\",\"required\":\"true\",\"defaultvalue\":\"1\"},{\"name\":\"p2\",\"description\":\"default description for parameter p2\",\"required\":\"true\",\"defaultvalue\":\"1\"}]}";
		jsonconfig = URLEncoder.encode(jsonconfig, StandardCharsets.US_ASCII);
		mvc.perform(MockMvcRequestBuilders.put("/price/setformulajsonconfiguration?jsonConfiguration="+jsonconfig).accept(MediaType.ALL))
	    	.andExpect(status().isOk());	
	}	
}