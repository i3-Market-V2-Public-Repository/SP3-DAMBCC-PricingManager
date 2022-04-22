package com.gft.i3market.cost.work;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

/**
 * 
 * @author A06F
 *
 */
public class CostWorker {

	private Environment env;
	
	private static final Logger logger = LogManager.getLogger(CostWorker.class);

	/**
	 * Constructor
	 */
	public CostWorker(Environment env) {
		this.env = env;
	}

	/**
	 * Calculate cost py price
	 * 
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public double calculateCost(String price) throws Exception {
		double y = 0;

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.cost"));

		//
		// EVAL FEE
		//
		try {
			logger.info("Eval fee");

			String feeS = properties.get("fee");

			double fee = Double.parseDouble(feeS);
			double priceD = Double.parseDouble(price);
			double unroundedCost = priceD * (fee / 100d);

			y = com.gft.i3market.utilities.Utilities.round(unroundedCost, 2);

			logger.info("Finish fee");
		} catch (Exception ex) {
			logger.error("Error eval fee: ", ex);
			throw ex;
		}

		return y;
	}

	/**
	 * Put Fee
	 * 
	 * @param fee
	 * @return
	 * @throws Exception
	 */
	public boolean putFee(String fee) throws Exception {
		try {
			// Chech if parameter is really a number
			Double.parseDouble(fee);

			Map<String, String> properties = new HashMap<String, String>();

			properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.cost"));

			if (properties.containsKey("fee"))
				properties.remove("fee");

			properties.put("fee", fee);

			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "",
					Paths.get("config/"+env.getProperty("property.file.cost")));
		} catch (Exception ex) {
			logger.error("Error updating fee formula: ", ex);
			throw ex;
		}

		return true;
	}
}
