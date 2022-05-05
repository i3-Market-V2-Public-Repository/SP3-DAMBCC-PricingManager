package com.gft.i3market.controllers;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.i3market.parameters.FormulaConfig;
import com.gft.i3market.parameters.FormulaConstantConfiguration;
import com.gft.i3market.parameters.FormulaParameterConfiguration;
import com.gft.i3market.parameters.FormulaWithConfiguration;
import com.gft.i3market.price.work.PriceWorker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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

	@Operation(summary = "Get the price of data")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "price of data",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/getprice", method = RequestMethod.GET)
	public ResponseEntity<String> getPrice(
			@Parameter(
					name =  "price",
					description = "current price of data",
					required = true)
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

		String value = String.valueOf(result);
		
		logger.info("Operation result ok: "+value);

		return new ResponseEntity<>(value, HttpStatus.OK);
	}

	/**
	 * Configure Formula Parameters
	 * 
	 * @param formulaParameter
	 * @return
	 */
	@Operation(summary = "Set formula parameter")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/setformulaparameter", method = RequestMethod.PUT)
	public ResponseEntity<String> configureFormulaParameters(
			@RequestBody FormulaParameterConfiguration formulaParameter)
			{
		// http://localhost:8080/price/putParam?name=b1&description=Credibility%20of%20seller&required=false&defaultvalue=2
		logger.info("configureformulaparameters/put");

		logger.info("Pramameter parameter: "+formulaParameter.toString());

		try {
			PriceWorker worker = new PriceWorker(env);

			worker.putFormulaParameter(formulaParameter);
		} catch (Exception e) {
			logger.error("Error putting parameter",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok");
		
		return new ResponseEntity<>("operation successful", HttpStatus.OK);
	}

	/**
	 * Configure Formula Constants
	 * 
	 * @param formulaConstant
	 * @return
	 */
	@Operation(summary = "Set formula constant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/setformulaconstant", method = RequestMethod.PUT)
	public ResponseEntity<String> configureFormulaConstants(
			@RequestBody FormulaConstantConfiguration formulaConstant)
			{
		// http://localhost:8080/price/putParam?name=b1&description=Credibility%20of%20seller&required=false&defaultvalue=2
		logger.info("configureformulaconstant/put");

		logger.info("Pramameter constant: "+formulaConstant.toString());

		try {
			PriceWorker worker = new PriceWorker(env);

			worker.putFormulaConstant(formulaConstant);
		} catch (Exception e) {
			logger.error("Error putting constant",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Operation result ok");
		
		return new ResponseEntity<>("operation successful",HttpStatus.OK);
	}

	@Operation(summary = "Set Formula with default values for constants and parameters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/setformulawithdefaultconfiguration", method = RequestMethod.PUT)
	public ResponseEntity<String> setFormulaWithConfiguration(
			@RequestBody FormulaWithConfiguration formulaJson)
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

		String result;

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

		return new ResponseEntity<>("operation successful",HttpStatus.OK);
	}

	@Operation(summary = "Check formula and parameters consistency")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/checkformulaconfiguration", method = RequestMethod.GET)
	public ResponseEntity<String> checkformulaconfiguration() {
		// example with f(p1, p2, p3, c1) =p1*1+p2*0.3+p3*0.5+c1
		logger.info("checkformulaconfiguration/get");

		String result = "Correct formula";

		try {
			PriceWorker worker = new PriceWorker(env);

			boolean check = worker.checkFormulaConfiguration();

			if (!check)
				result = "Incorrect formula";

		} catch (Exception e) {
			logger.error("Error checking formula",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok");
		
		return ResponseEntity.ok(result);
	}

	@Operation(summary = "Get configuration using Json format")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = FormulaConfig.class)) })
	})
	@RequestMapping(value = "/getformulajsonconfiguration", method = RequestMethod.GET)
	@ResponseBody
	public FormulaConfig getFormulaConfiguration() {
		// http://localhost:8080/price/getformulaconfiguration
		logger.info("getformula/get");

		String result = "";

		FormulaConfig formulaConfig = new FormulaConfig();
		try {
			PriceWorker worker = new PriceWorker(env);

			String formula = worker.getFormula();
			FormulaConstantConfiguration[] constants = worker.getFormulaConstantsObject();
			FormulaParameterConfiguration[] parameters = worker.getFormulaParamsObject();

			formulaConfig.setFormula(formula);
			formulaConfig.setFormulaConstantConfiguration(constants);
			formulaConfig.setFormulaParameterConfiguration(parameters);

		} catch (Exception e) {
			logger.error("Error getting formula configuration",e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		logger.info("Operation result ok: "+ formulaConfig.getFormula());

		return formulaConfig;
	}

	@Operation(summary = "Set configuration using Json format")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/setformulajsonconfiguration", method = RequestMethod.PUT)
	public ResponseEntity<String> setFormulaConfiguration(
			@RequestBody FormulaConfig jsonConfiguration)
		{
		// http://localhost:8080/price/setformulaconfiguration?{"formula":" f(p1, p2, p3, c1) =p1*1+p2*0.3+p3*0.5+c1","formulaConstantConfiguration":[{"name":"c1","description":"Constantc1_description","value":"Constantc1_value"}],"formulaParameterConfiguration":[{"name":"p1","description":"Parameter_p1_description","required":"true","defaultvalue":"Parameter_p1_defaultvalue"},{"name":"p2","description":"Parameter_p2_description","required":"true","defaultvalue":"Parameter_p2_defaultvalue"},{"name":"p3","description":"Parameter_p3_description","required":"true","defaultvalue":"Parameter_p3_defaultvalue"}]}
		logger.info("setformulaconfiguration/put");

		logger.info("Pramameter jsonConfiguration: "+jsonConfiguration);

		try {

			PriceWorker worker = new PriceWorker(env);

			worker.setFormulaConfig(jsonConfiguration);

		} catch (Exception e) {
			logger.error("Error setting formula configuration",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok");

		return new ResponseEntity<>("operation successful",HttpStatus.OK);
	}
}