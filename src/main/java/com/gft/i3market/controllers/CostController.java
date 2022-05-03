package com.gft.i3market.controllers;

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

import io.swagger.annotations.ApiOperation;

/**
 * CostController
 *
 * @author lorenzo
 *
 */

@RestController
@RequestMapping("/cost")
public class CostController {

	@Autowired
	private Environment env;
	
	private static final Logger logger = LogManager.getLogger(CostController.class);
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	@ApiOperation(value = "Get the cost of item", response = Iterable.class)
	@RequestMapping(value = "/getcost", method = RequestMethod.GET)
	public ResponseEntity<String> get(
			@RequestParam(value= "price" ,required=true) String price) 
			{

		logger.info("getcost/get");

		double result = 0;

		try {
			CostWorker worker = new CostWorker(env);

			result = worker.calculateCost(price);
		} catch (Exception e) {
			logger.error("Error calculating cost",e);
			return ResponseEntity.ok("Bad request: " + e.toString());
		}

		String returString = String.valueOf(result);

		logger.info("Operation result ok: "+returString);
		
		return ResponseEntity.ok(returString);
	}

	/**
	 * 
	 * @param fee
	 * @return
	 */
	@ApiOperation(value = "Set fee", response = Iterable.class)
	@RequestMapping(value = "/setfee", method = RequestMethod.PUT)
	//@RequestMapping("/putFee")
	public ResponseEntity<String> put(String fee) {
		
		logger.info("setfee/put");

		String returString = "OK";

		try {
			CostWorker worker = new CostWorker(env);

			worker.putFee(fee);
		} catch (Exception e) {
			logger.error("Error setting fee",e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		logger.info("Operation result ok: "+returString);
		
		return new ResponseEntity<>(returString, HttpStatus.OK);
	}
}