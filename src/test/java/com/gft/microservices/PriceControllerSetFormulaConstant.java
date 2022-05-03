package com.gft.microservices;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.i3market.I3MarketApplication;
import com.gft.i3market.parameters.FormulaConstantConfiguration;
import com.gft.i3market.parameters.FormulaWithConfiguration;

@AutoConfigureMockMvc
@SpringBootTest(classes = I3MarketApplication.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.properties") // OVERRIDE ENVIROMENT
@TestMethodOrder(OrderAnnotation.class)
@Order(10) 
public class PriceControllerSetFormulaConstant {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Environment env;
	
	@Test
	@Order(10) 
	public void setFormulaConstant() throws Exception {
				
		// Configure formula using default values:
		// formula=f(p1,c1)=p1+c1
		// p1 required true default value as default (1)
		// c1 value = 1
		ObjectMapper objectMapper = new ObjectMapper();
		
		FormulaWithConfiguration formulaWithConfiguration = new FormulaWithConfiguration();
		formulaWithConfiguration.setFormula("f(p1,c1)=p1+c1");
		formulaWithConfiguration.setContantslist("c1");
		formulaWithConfiguration.setParameterslist("p1");
		
		String jsonconfig = objectMapper.writeValueAsString(formulaWithConfiguration);

		mvc.perform(put("/price/setformulawithdefaultconfiguration").contentType(MediaType.APPLICATION_JSON).content(jsonconfig))
		.andExpect(status().isOk());
		
		// TEST 10
		// Change the value of constant
		// c1 = 3
		FormulaConstantConfiguration formulaParameter = new FormulaConstantConfiguration();
		formulaParameter.setName("c1");
		formulaParameter.setDescription("c1test");
		formulaParameter.setValue("3");
		
		jsonconfig = objectMapper.writeValueAsString(formulaParameter);

		mvc.perform(put("/price/setformulaconstant").contentType(MediaType.APPLICATION_JSON).content(jsonconfig))
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
	}
}