package com.gft.i3market.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gft.i3market.cost.work.CostWorker;
import com.gft.i3market.parameters.FormulaParameterConfiguration;


/**
 * CostController
 *
 * @author lorenzo
 *
 */

@RestController
@RequestMapping("/fee")
public class CostController {

	@Autowired
	private Environment env;
	
	private static final Logger logger = LogManager.getLogger(CostController.class);
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	@Operation(summary = "get I3M fee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/getfee", method = RequestMethod.GET)
	public ResponseEntity<String> get(
			@Parameter(
					name =  "price",
					description = "current price of the data",
					example = "150",
					required = true)
			@RequestParam(value= "price" ,required=true) String price)
			{

		logger.info("fee/getfee");

		double result = 0;

		try {
			CostWorker worker = new CostWorker(env);

			result = worker.calculateCost(price);
		} catch (Exception e) {
			logger.error("Error calculating cost",e);
			return ResponseEntity.ok("Bad request: " + e.toString());
		}

		String value = String.valueOf(result);

		return new ResponseEntity<>(value,HttpStatus.OK);
	}

	/**
	 * 
	 * @param fee
	 * @return
	 */
	@Operation(summary = "set I3M fee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					content = { @Content(mediaType = "text/plain",
							schema = @Schema(implementation = String.class)) })
	})
	@RequestMapping(value = "/setfee", method = RequestMethod.PUT)
	public ResponseEntity<String> put(String fee) {
		
		logger.info("fee/setfee");

		try {
			CostWorker worker = new CostWorker(env);

			worker.putFee(fee);
		} catch (Exception e) {
			logger.error("Error setting fee",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("operation successful",HttpStatus.OK);
	}
}