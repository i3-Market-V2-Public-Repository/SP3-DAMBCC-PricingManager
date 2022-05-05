package com.gft.i3market.price.work;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.gft.i3market.parameters.FormulaConfig;
import com.gft.i3market.parameters.FormulaConstantConfiguration;
import com.gft.i3market.parameters.FormulaParameterConfiguration;

/**
 * 
 * @author A06F
 *
 */
public class PriceWorker {

	private Environment env;
	
	private static final Logger logger = LogManager.getLogger(PriceWorker.class);

	/**
	 * Constructor
	 */
	public PriceWorker(Environment env) {
		this.env = env;
	}

	/**
	 * Calculate price
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public double calculatePrice(Map<String, String> parameters) throws Exception {
		double y = 0;

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		//
		// READ REQUEST PARAMETERS
		//
		com.gft.i3market.utilities.Utilities.logRequestParameters(parameters);

		//
		// CONFIGURE FORMULA
		//
		FormulaConfiguration formulaConfiguration = new FormulaConfiguration();
		try {
			logger.info("Configuring formula by properties");

			formulaConfiguration.Configure(properties);

			logger.info("Configuration done");
		} catch (Exception ex) {
			logger.error("Error configuring formula: ", ex);
			throw ex;
		}

		//
		// EVAL FORMULA
		//
		FormulaWorker formulaWorker = new FormulaWorker();
		try {
			logger.info("Eval formula");

			y = formulaWorker.calculateFormula(formulaConfiguration, parameters);

			logger.info("Finish Eval");
		} catch (Exception ex) {
			logger.error("Error eval formula: ", ex);
			throw ex;
		}

		return y;
	}

	/**
	 * Put constant in config property file
	 * 
	 * @param formulaParameter
	 * @return
	 * @throws Exception
	 */
	public boolean putFormulaParameter(FormulaParameterConfiguration formulaParameter) throws Exception {

		//
		// VALIDATE PARAMS
		//
		formulaParameter.validate();

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		try {
			// Check presence in constants defined in propery file
			// if present update values
			// if not exists create it

			String[] parametersArray = com.gft.i3market.utilities.Utilities.readParametersFromProperties(properties);

			boolean existsConstant = false;
			for (int i = 0; i < parametersArray.length; i++) {
				String costant = parametersArray[i];

				if (costant.equals(formulaParameter.getName())) {
					existsConstant = true;
					break;
				}
			}

			// update existing config values ore creates it (deleting ad recreating always)
			/*
			 * Parameter_p3_defaultvalue=1 Parameter_p3_description=default description for
			 * parameter p3 Parameter_p3_required=true
			 */

			String name = formulaParameter.getName();
			String desc = "Parameter_" + name + "_description";
			String defaultvalue = "Parameter_" + name + "_defaultvalue";
			String required = "Parameter_" + name + "_required";

			if (properties.containsKey(desc))
				properties.remove(desc);
			if (properties.containsKey(defaultvalue))
				properties.remove(defaultvalue);
			if (properties.containsKey(required))
				properties.remove(required);

			properties.put(desc, formulaParameter.getDescription());
			properties.put(defaultvalue, formulaParameter.getDefaultvalue());
			properties.put(required, formulaParameter.getRequired());

			logger.info("Adding '"+desc+"':"+formulaParameter.getDescription());
			logger.info("Adding '"+defaultvalue+"':"+formulaParameter.getDefaultvalue());
			logger.info("Adding '"+required+"':"+formulaParameter.getRequired());
			
			if (!existsConstant) {
				String constants_list = (String) properties.get("Parameters");
				if (constants_list.length() > 1) {
					constants_list = constants_list + ";" + name;
				} else {
					constants_list = name;
				}

				logger.info("Adding 'Parameters property':"+constants_list);
				if (properties.containsKey("Parameters"))
					properties.remove("Parameters");
				properties.put("Parameters", constants_list);				
			}

			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "",
					Paths.get("config/"+env.getProperty("property.file.price")));
		} catch (Exception ex) {
			logger.error("Error updating data: ", ex);
			throw ex;
		}

		return true;
	}

	/**
	 * Put constant in config property file
	 * 
	 * @param putConstParam
	 * @return
	 * @throws Exception
	 */
	public boolean putFormulaConstant(FormulaConstantConfiguration formulaParameter) throws Exception {

		//
		// VALIDATE PARAMS
		//
		formulaParameter.validate();

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		try {
			// Check presence in constants defined in propery file
			// if present update values
			// if not exists create it

			String[] parametersArray = com.gft.i3market.utilities.Utilities.readConstantFromProperties(properties);

			boolean existsConstant = false;
			for (int i = 0; i < parametersArray.length; i++) {
				String costant = parametersArray[i];

				if (costant.equals(formulaParameter.getName())) {
					existsConstant = true;
					break;
				}
			}

			// update existing config values ore creates it (deleting ad recreating always)
			/*
			 * Constant_b1_description=b1 Constant_b1_value=1
			 */

			String name = formulaParameter.getName();
			String desc = "Constant_" + name + "_description";
			String defaultvalue = "Constant_" + name + "_value";

			if (properties.containsKey(desc))
				properties.remove(desc);
			if (properties.containsKey(defaultvalue))
				properties.remove(defaultvalue);

			properties.put(desc, formulaParameter.getDescription());
			properties.put(defaultvalue, formulaParameter.getValue());

			logger.info("Adding '"+desc+"':"+formulaParameter.getDescription());
			logger.info("Adding '"+defaultvalue+"':"+formulaParameter.getValue());
			
			if (!existsConstant) {
				String constants_list = (String) properties.get("Constants");
				if (constants_list.length() > 1) {
					constants_list = constants_list + ";" + name;
				} else {
					constants_list = name;
				}

				logger.info("Adding 'Parameters Constants':"+constants_list);
				if (properties.containsKey("Constants"))
					properties.remove("Constants");
				properties.put("Constants", constants_list);
			}

			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "",
					Paths.get("config/"+env.getProperty("property.file.price")));
		} catch (Exception ex) {
			logger.error("Error updating data: ", ex);
			throw ex;
		}

		return true;
	}

	/**
	 * Get Formula
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFormula() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String formula = "";
		//
		// READ FORMULA
		//
		try {
			formula = (String) properties.get("Formula");
		} catch (Exception ex) {
			logger.error("Error reading formula: ", ex);
			throw ex;
		}

		return formula;
	}

	/**
	 * Read forumla constnts in property fils
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getFormulaConstants() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String formula[] = new String[0];

		try {

			formula = com.gft.i3market.utilities.Utilities.readConstantFromProperties(properties);

			for (int i = 0; i < formula.length; i++) {
				String constantName = formula[i];

				String desc = "Constant_" + constantName + "_description";
				String val = "Constant_" + constantName + "_value";

				String descval = "";
				String valval = "";

				if (properties.containsKey(desc))
					descval = properties.get(desc);

				if (properties.containsKey(val))
					valval = properties.get(val);

				formula[i] = "Name:" + constantName + " Value:" + valval + " Description:" + descval;
			}

		} catch (Exception ex) {
			logger.error("Error retrieving properties: ", ex);
			throw ex;
		}

		return formula;
	}

	/**
	 * Get formula constant configuration as object
	 * @return
	 * @throws Exception
	 */
	public FormulaConstantConfiguration[] getFormulaConstantsObject() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		FormulaConstantConfiguration[] formulaConstantConfiguration = new FormulaConstantConfiguration[0];

		try {

			String[] formulaData = com.gft.i3market.utilities.Utilities.readConstantFromProperties(properties);

			formulaConstantConfiguration = new FormulaConstantConfiguration[formulaData.length];

			for (int i = 0; i < formulaData.length; i++) {
				FormulaConstantConfiguration con = new FormulaConstantConfiguration();

				String constantName = formulaData[i];

				/*
				 * Constant_c1_description=default description for constant c1
				 * Constant_c1_value=1
				 */
				String desc = "Constant_" + constantName + "_description";
				String val = "Constant_" + constantName + "_value";

				String descval = "";
				String valval = "";

				if (properties.containsKey(desc))
					descval = properties.get(desc);

				if (properties.containsKey(val))
					valval = properties.get(val);

				con.setName(constantName);
				con.setValue(valval);
				con.setDescription(descval);

				formulaConstantConfiguration[i] = con;
			}

		} catch (Exception ex) {
			logger.error("Error retrieving constants: ", ex);
			throw ex;
		}

		return formulaConstantConfiguration;
	}

	/**
	 * Get Formula Parameters Description
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getFormulaParams() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String formula[] = new String[0];

		try {

			formula = com.gft.i3market.utilities.Utilities.readParametersFromProperties(properties);

			for (int i = 0; i < formula.length; i++) {
				String constantName = formula[i];

				/*
				 * Parameter_exclusivityofaccess_description=Exclusivity of access
				 * Parameter_exclusivityofaccess_required=true
				 * Parameter_exclusivityofaccess_defaultvalue=1
				 */
				String desc = "Parameter_" + constantName + "_description";
				String required = "Parameter_" + constantName + "_required";
				String val = "Parameter_" + constantName + "_defaultvalue";

				String descval = "";
				String valval = "";
				String requiredval = "";

				if (properties.containsKey(desc))
					descval = properties.get(desc);

				if (properties.containsKey(val))
					valval = properties.get(val);

				if (properties.containsKey(required))
					requiredval = properties.get(required);

				formula[i] = "Name:" + constantName + " Required:" + requiredval + " Default:" + valval
						+ " Description:" + descval;
			}

		} catch (Exception ex) {
			logger.error("Error retrieving constants: ", ex);
			throw ex;
		}

		return formula;
	}

	/**
	 * Get formula parameters configuration as object
	 * @return
	 * @throws Exception
	 */
	public FormulaParameterConfiguration[] getFormulaParamsObject() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		FormulaParameterConfiguration[] formulaParameterConfiguration = new FormulaParameterConfiguration[0];

		try {

			String[] formulaData = com.gft.i3market.utilities.Utilities.readParametersFromProperties(properties);

			formulaParameterConfiguration = new FormulaParameterConfiguration[formulaData.length];

			for (int i = 0; i < formulaData.length; i++) {
				FormulaParameterConfiguration con = new FormulaParameterConfiguration();

				String constantName = formulaData[i];

				/*
				 * Parameter_exclusivityofaccess_description=Exclusivity of access
				 * Parameter_exclusivityofaccess_required=true
				 * Parameter_exclusivityofaccess_defaultvalue=1
				 */
				String desc = "Parameter_" + constantName + "_description";
				String required = "Parameter_" + constantName + "_required";
				String val = "Parameter_" + constantName + "_defaultvalue";

				String descval = "";
				String valval = "";
				String requiredval = "";

				if (properties.containsKey(desc))
					descval = properties.get(desc);

				if (properties.containsKey(val))
					valval = properties.get(val);

				if (properties.containsKey(required))
					requiredval = properties.get(required);

				con.setName(constantName);
				con.setDefaultvalue(valval);
				con.setRequired(requiredval);
				con.setDescription(descval);

				formulaParameterConfiguration[i] = con;
			}

		} catch (Exception ex) {
			logger.error("Error retrieving constants: ", ex);
			throw ex;
		}

		return formulaParameterConfiguration;
	}

	/**
	 * Set Formula
	 * 
	 * @param formula
	 * @return
	 * @throws Exception
	 */
	public String setFormula(String formula) throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String retval = "";
		//
		// CHECK FORMULA
		//
		try {
			formula = formula.replace("\\=", "=");
			Function f = new Function(formula);

			boolean checkok = f.checkSyntax();

			if (!checkok) {
				throw new Exception("Invalid formula syntax");
			}

			//
			// delete parameters and constants
			//
			List<String> removeRange = new ArrayList<String>();
			for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext();) {
				String currKey = (String) iterator.next();
				if (currKey.contains("Parameter_") || currKey.contains("Constant_")
						|| currKey.equalsIgnoreCase("Constants") || currKey.equalsIgnoreCase("Parameters")
						|| currKey.equalsIgnoreCase("Formula")) {
					removeRange.add(currKey);
				}
			}
			for (int i = 0; i < removeRange.size(); i++) {
				properties.remove(removeRange.get(i));
			}
			properties.put("Formula", formula);
			properties.put("Parameters", "");
			properties.put("Constants", "");
			/*
			 * Integer numArgs = f.getArgumentsNumber(); for(int i= 0 ; i<numArgs; i++) {
			 * Argument argument = f.getArgument(i);
			 * System.out.println(argument.getArgumentName()); }
			 */

			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "put by request",
					Paths.get("config/"+env.getProperty("property.file.price")));

		} catch (Exception ex) {
			logger.error("Error checking formula: ", ex);
			throw ex;
		}

		return retval;
	}

	/**
	 * Create config constants
	 * 
	 * @param constantList
	 * @throws Exception
	 */
	public void createConfigConstants(String constantList) throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String retval = "";
		//
		// CHECK FORMULA
		//
		try {
			String formula = properties.get("Formula");

			Function f = new Function(formula);

			boolean checkok = f.checkSyntax();

			if (!checkok) {
				throw new Exception("Invalid formula syntax");
			}

			//
			// delete constants
			//
			List<String> removeRange = new ArrayList<String>();
			for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext();) {
				String currKey = (String) iterator.next();
				if (currKey.contains("Constant_") || currKey.equalsIgnoreCase("Constants")) {
					removeRange.add(currKey);
				}
			}
			for (int i = 0; i < removeRange.size(); i++) {
				properties.remove(removeRange.get(i));
			}

			//
			// CHECK CONSTANTS CONGRUENCE
			//
			String[] reqParams = new String[0];
			if (constantList.contains(",")) {
				reqParams = constantList.split(",");
			} else {
				reqParams = new String[1];
				reqParams[0] = constantList;
			}

			Integer foundArgs = 0;
			for (int i = 0; i < reqParams.length; i++) {
				Integer numArgs = f.getArgumentsNumber();
				for (int ii = 0; ii < numArgs; ii++) {
					Argument argument = f.getArgument(ii);
					String argumentName = argument.getArgumentName();
					if (reqParams[i].equals(argumentName))
						foundArgs++;
				}
			}

			if (foundArgs != reqParams.length) {
				throw new Exception("Invalid configuration");
			}

			//
			// ADD CONSTANTS
			//
			for (int i = 0; i < reqParams.length; i++) {
				String constantName = reqParams[i];
				String desc = "Constant_" + constantName + "_description";
				String val = "Constant_" + constantName + "_value";

				properties.put(desc, "default description for constant " + constantName);
				properties.put(val, "1");
			}

			properties.put("Constants", constantList.replaceAll(",", ";"));

			//
			// SAVE PROPERTIES
			//
			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "put by request",
					Paths.get("config/"+env.getProperty("property.file.price")));

		} catch (Exception ex) {
			logger.error("Error creating formula constant: ", ex);
			throw ex;
		}

		return;
	}

	/**
	 * Create config parameters
	 * 
	 * @param constantList
	 * @throws Exception
	 */
	public void createConfigParameters(String constantList) throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		String retval = "";
		//
		// CHECK FORMULA
		//
		try {
			String formula = properties.get("Formula");

			Function f = new Function(formula);

			boolean checkok = f.checkSyntax();

			if (!checkok) {
				throw new Exception("Invalid formula syntax");
			}

			//
			// delete constants
			//
			List<String> removeRange = new ArrayList<String>();
			for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext();) {
				String currKey = (String) iterator.next();
				if (currKey.contains("Parameter_") || currKey.equalsIgnoreCase("Parameters")) {
					removeRange.add(currKey);
				}
			}
			for (int i = 0; i < removeRange.size(); i++) {
				properties.remove(removeRange.get(i));
			}

			//
			// CHECK CONSTANTS CONGRUENCE
			//
			String[] reqParams = new String[0];
			if (constantList.contains(",")) {
				reqParams = constantList.split(",");
			} else {
				reqParams = new String[1];
				reqParams[0] = constantList;
			}

			Integer foundArgs = 0;
			for (int i = 0; i < reqParams.length; i++) {
				Integer numArgs = f.getArgumentsNumber();
				for (int ii = 0; ii < numArgs; ii++) {
					Argument argument = f.getArgument(ii);
					String argumentName = argument.getArgumentName();
					if (reqParams[i].equals(argumentName))
						foundArgs++;
				}
			}

			if (foundArgs != reqParams.length) {
				throw new Exception("Invalid configuration");
			}

			//
			// ADD PROPERTIES
			//
			for (int i = 0; i < reqParams.length; i++) {
				String constantName = reqParams[i];
				String desc = "Parameter_" + constantName + "_description";
				String required = "Parameter_" + constantName + "_required";
				String val = "Parameter_" + constantName + "_defaultvalue";

				properties.put(desc, "default description for parameter " + constantName);
				properties.put(required, "true");
				properties.put(val, "1");
			}

			properties.put("Parameters", constantList.replaceAll(",", ";"));

			//
			// SAVE PROPERTIES
			//
			com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties, "put by request",
					Paths.get("config/"+env.getProperty("property.file.price")));

		} catch (Exception ex) {
			logger.error("Error checking formula: ", ex);
			throw ex;
		}

		return;
	}

	/**
	 * Check formula configuration
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean checkFormulaConfiguration() throws Exception {

		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();

		properties = com.gft.i3market.utilities.Utilities.readProperties("config/"+env.getProperty("property.file.price"));

		boolean retval = true;
		//
		// CHECK FORMULA
		//
		try {
			String formula = properties.get("Formula");

			Function f = new Function(formula);

			boolean checkok = f.checkSyntax();

			if (!checkok) {
				throw new Exception("Invalid formula syntax");
			}

			String constantList = properties.get("Constants");
			//
			// CHECK CONSTANTS CONGRUENCE
			//
			String[] confingConstants = new String[0];
			if (constantList.contains(";")) {
				confingConstants = constantList.split(";");
			} else {
				confingConstants = new String[1];
				confingConstants[0] = constantList;
			}

			Integer foundCArgs = 0;
			for (int i = 0; i < confingConstants.length; i++) {
				Integer numArgs = f.getArgumentsNumber();
				for (int ii = 0; ii < numArgs; ii++) {
					Argument argument = f.getArgument(ii);
					String argumentName = argument.getArgumentName();
					if (confingConstants[i].equals(argumentName))
						foundCArgs++;
				}
			}

			if (foundCArgs != confingConstants.length) {
				return false;
			}

			String propertiesList = properties.get("Parameters");
			//
			// CHECK PARAMETERS CONGRUENCE
			//
			String[] configParams = new String[0];
			if (propertiesList.contains(";")) {
				configParams = propertiesList.split(";");
			} else {
				configParams = new String[1];
				configParams[0] = propertiesList;
			}

			Integer foundPArgs = 0;
			for (int i = 0; i < configParams.length; i++) {
				Integer numArgs = f.getArgumentsNumber();
				for (int ii = 0; ii < numArgs; ii++) {
					Argument argument = f.getArgument(ii);
					String argumentName = argument.getArgumentName();
					if (configParams[i].equals(argumentName))
						foundPArgs++;
				}
			}

			if (foundPArgs != configParams.length) {
				return false;
			}

			Integer sum = foundPArgs + foundCArgs;
			if (sum != f.getArgumentsNumber()) {
				return false;
			}

		} catch (Exception ex) {
			logger.error("Error checking formula: ", ex);
			throw ex;
		}

		return retval;
	}

	/**
	 * Set Formula config in JSON format
	 * 
	 * @param formulaConfig
	 * @throws Exception
	 */
	public void setFormulaConfig(FormulaConfig formulaConfig) throws Exception {
		//
		// CHECK FORMULA
		//
		try {
			String formula = formulaConfig.getFormula();

			// create formula in prop file
			this.setFormula(formula);

			/*
			 * // add parameters and constants propertyes Map<String, String> properties =
			 * com.gft.i3market.utilities.Utilities.readProperties("config/price.properties"
			 * );
			 * 
			 * properties.put("Parameters", ""); properties.put("Constants", "");
			 * 
			 * com.gft.i3market.utilities.Utilities.saveSortedPropertiesToFile(properties,
			 * "put by request", Paths.get("config/price.properties"));
			 */

			// configure constants
			for (int ci = 0; ci < formulaConfig.getFormulaConstantConfiguration().length; ci++) {
				FormulaConstantConfiguration formulaParameter = formulaConfig.getFormulaConstantConfiguration()[ci];

				putFormulaConstant(formulaParameter);
			}

			// configure parameters
			for (int pi = 0; pi < formulaConfig.getFormulaParameterConfiguration().length; pi++) {
				FormulaParameterConfiguration formulaParameter = formulaConfig.getFormulaParameterConfiguration()[pi];

				putFormulaParameter(formulaParameter);
			}

			// check formula
			boolean valid = this.checkFormulaConfiguration();

			if (!valid)
				throw new Exception("Check formula error");

		} catch (Exception ex) {
			logger.error("Error checking formula: ", ex);
			throw ex;
		}

		return;
	}
}