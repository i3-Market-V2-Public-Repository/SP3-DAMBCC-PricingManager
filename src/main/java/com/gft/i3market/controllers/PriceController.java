package com.gft.i3market.controllers;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.i3market.parameters.FormulaConfig;
import com.gft.i3market.parameters.FormulaConstantConfiguration;
import com.gft.i3market.parameters.FormulaParameterConfiguration;
import com.gft.i3market.parameters.FormulaWithConfiguration;
import com.gft.i3market.price.work.PriceWorker;
import com.gft.test.jackson.Car;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Price controller
 *
 * @author lorenzo
 *
 */

@RestController
@RequestMapping("/price")
public class PriceController {

	@Autowired
	private Environment env;
	
	private static final Logger logger = LogManager.getLogger(PriceController.class);

	/**
	 * Get price by parameters
	 * 
	 * @param parameters
	 * @return
	 */
	@ApiOperation(value = "Get the price of item", response = Iterable.class)
	@RequestMapping(value = "/getprice", method = RequestMethod.GET)
	public ResponseEntity<String> getPrice(
			@RequestParam Map<String, String> parameters)
			{
		// http://localhost:8080/price/getprice?credibilityoftheseller=1&ageofdata=1&accuracyofdata=1&volumeofdata=1&costofcollectingandstorage=1&riskofprivacyviolations=1&exclusivityofaccess=1&rawdatavsprocesseddata=1&levelofownership=1
		logger.info("getprice/get");

		try {
			parameters = com.gft.i3market.utilities.Utilities.adjustSwaggerParams(parameters);
		} catch (Exception e) {
			logger.error("Error adjusting parameters",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		double result = 0;

		try {
			PriceWorker worker = new PriceWorker(env);

			result = worker.calculatePrice(parameters);
			
		} catch (Exception e) {
			logger.error("Error calculating price",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		String returString = String.valueOf(result);
		
		logger.info("Operation result ok: "+returString);
		
		return ResponseEntity.ok(returString);
	}

	/**
	 * Configure Formula Parameters
	 * 
	 * @param putConstParam
	 * @return
	 */
	@ApiOperation(value = "Set formula parameter", response = Iterable.class)
	@RequestMapping(value = "/setformulaparameter", method = RequestMethod.PUT)
	public ResponseEntity<String> configureFormulaParameters(
			@RequestBody(required=true) FormulaParameterConfiguration formulaParameter) 
			{
		// http://localhost:8080/price/putParam?name=b1&description=Credibility%20of%20seller&required=false&defaultvalue=2
		logger.info("configureformulaparameters/put");

		logger.info("Pramameter parameter: "+formulaParameter.toString());

		String returString = "OK";

		try {
			PriceWorker worker = new PriceWorker(env);

			worker.putFormulaParameter(formulaParameter);
		} catch (Exception e) {
			logger.error("Error putting parameter",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok: "+returString);
		
		return new ResponseEntity<>(returString, HttpStatus.OK);
	}

	/**
	 * Configure Formula Contstants
	 * 
	 * @param putConstParam
	 * @return
	 */
	@ApiOperation(value = "Set formula constant", response = Iterable.class)
	@RequestMapping(value = "/setformulaconstant", method = RequestMethod.PUT)
	public ResponseEntity<String> configureFormulaParameters(
			@RequestBody(required=true) FormulaConstantConfiguration formulaParameter)
			{
		// http://localhost:8080/price/putParam?name=b1&description=Credibility%20of%20seller&required=false&defaultvalue=2
		logger.info("configureformulaconstant/put");

		logger.info("Pramameter constant: "+formulaParameter.toString());

		String returString = "OK";

		try {
			PriceWorker worker = new PriceWorker(env);

			worker.putFormulaConstant(formulaParameter);
		} catch (Exception e) {
			logger.error("Error putting constant",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Operation result ok: "+returString);
		
		return new ResponseEntity<>(returString, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Set Formula with default values for constants and parameters", response = Iterable.class)
	@RequestMapping(value = "/setformulawithdefaultconfiguration", method = RequestMethod.PUT)
	public ResponseEntity<String> setFormulaWithConfiguration(
			@RequestBody(required=true) FormulaWithConfiguration formulaJson) 
	{
		String formula = formulaJson.getFormula();
		String contantslist = formulaJson.getContantslist();
		String parameterslist =formulaJson.getParameterslist();
		
		// example with f(p1, p2, p3, c1) =p1*1+p2*0.3+p3*0.5+c1
		// c1
		// p1,p2,p3
		logger.info("setformula/put");

		logger.info("Pramameter formula: "+formula);
		logger.info("Pramameter constant list: "+contantslist);
		logger.info("Pramameter parameter list: "+parameterslist);
		
		String result = "OK";

		try {
			PriceWorker worker = new PriceWorker(env);

			result = worker.setFormula(formula);
			worker.createConfigConstants(contantslist);
			worker.createConfigParameters(parameterslist);

		} catch (Exception e) {
			logger.error("Error configuring formula",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok");
		
		return ResponseEntity.ok(result);
	}

	@ApiOperation(value = "Check formula and parameters consistency", response = Iterable.class)
	@RequestMapping(value = "/checkformulaconfiguration", method = RequestMethod.GET)
	public ResponseEntity<String> checkformulaconfiguration() {
		// example with f(p1, p2, p3, c1) =p1*1+p2*0.3+p3*0.5+c1
		logger.info("checkformulaconfiguration/get");

		String result = "OK";

		try {
			PriceWorker worker = new PriceWorker(env);

			boolean check = worker.checkFormulaConfiguration();

			if (!check)
				result = "KO";

		} catch (Exception e) {
			logger.error("Error checling formula",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok");
		
		return ResponseEntity.ok(result);
	}
	
	@ApiOperation(value = "Get configuration using Json format", response = Iterable.class)
	@RequestMapping(value = "/getformulajsonconfiguration", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getFormulaConfiguration() {
		// http://localhost:8080/price/getformulaconfiguration
		logger.info("getformula/get");

		String result = "";

		FormulaConfig formulaConfig = new FormulaConfig();
		try {
			PriceWorker worker = new PriceWorker(env);

			String forula = worker.getFormula();
			FormulaConstantConfiguration[] constants = worker.getFormulaConstantsObject();
			FormulaParameterConfiguration[] parameters = worker.getFormulaParamsObject();
			
			formulaConfig.setFormula(forula);
			formulaConfig.setFormulaConstantConfiguration(constants);
			formulaConfig.setFormulaParameterConfiguration(parameters);
			
			ObjectMapper objectMapper = new ObjectMapper();

			result = objectMapper.writeValueAsString(formulaConfig);

		} catch (Exception e) {
			logger.error("Error getting formula configuration",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok: "+result);
		
		return ResponseEntity.ok(result);
	}
	
	@ApiOperation(value = "Set configuration using Json format", response = Iterable.class)
	@RequestMapping(value = "/setformulajsonconfiguration", method = RequestMethod.PUT)
	public ResponseEntity<String> setFormulaConfiguration(
			@RequestBody(required=true) String jsonConfiguration)
		{
		// http://localhost:8080/price/setformulaconfiguration?{"formula":" f(p1, p2, p3, c1) =p1*1+p2*0.3+p3*0.5+c1","formulaConstantConfiguration":[{"name":"c1","description":"Constantc1_description","value":"Constantc1_value"}],"formulaParameterConfiguration":[{"name":"p1","description":"Parameter_p1_description","required":"true","defaultvalue":"Parameter_p1_defaultvalue"},{"name":"p2","description":"Parameter_p2_description","required":"true","defaultvalue":"Parameter_p2_defaultvalue"},{"name":"p3","description":"Parameter_p3_description","required":"true","defaultvalue":"Parameter_p3_defaultvalue"}]}
		logger.info("setformulaconfiguration/put");

		logger.info("Pramameter jsonConfiguration: "+jsonConfiguration);
		
		String result = "OK";

		try {
			FormulaConfig formulaConfig = new FormulaConfig();
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			//jsonConfiguration = URLDecoder.decode(jsonConfiguration, StandardCharsets.US_ASCII);
			
			formulaConfig = objectMapper.readValue(jsonConfiguration, FormulaConfig.class);	
			
			PriceWorker worker = new PriceWorker(env);

			worker.setFormulaConfig(formulaConfig);

		} catch (Exception e) {
			logger.error("Error setting formula configuration",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok: "+result);
		
		return ResponseEntity.ok(result);
	}
}