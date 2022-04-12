package com.gft.i3market.price.work;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FormulaConfiguration {

	private static final Logger logger = LogManager.getLogger(FormulaConfiguration.class);

	public FormulaConfiguration() {

	}

	private String formula = "";
	private HashMap<String, FormulaArgument> formulaArguments;
	private HashMap<String, FormulaConstant> formulaConstants;

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public HashMap<String, FormulaArgument> getFormulaArguments() {
		return formulaArguments;
	}

	public void setFormulaArguments(HashMap<String, FormulaArgument> formulaArguments) {
		this.formulaArguments = formulaArguments;
	}

	public HashMap<String, FormulaConstant> getFormulaConstants() {
		return formulaConstants;
	}

	public void setFormulaConstants(HashMap<String, FormulaConstant> formulaConstants) {
		this.formulaConstants = formulaConstants;
	}

	@Override
	public String toString() {
		return "FormulaConfiguration [formula=" + formula + ", formulaArguments=" + formulaArguments
				+ ", formulaConstants=" + formulaConstants + "]";
	}

	/**
	 * Configure formula by property file
	 * @param properties
	 * @throws Exception
	 */
	public void Configure(Map properties) throws Exception {
		try {
			// read formula
			logger.debug("Reading formulat");
			if (properties.containsKey("Formula")) {
				this.formula = (String) properties.get("Formula");
				logger.debug("formula: " + this.formula);
			} else {
				throw new Exception("Formula not defined. Ad 'Formula' in config.properties");
			}

			//
			// READ PROPERTIES PARAMETERS
			//
			logger.debug("Reading configured parameters");
			String[] parametersArray = com.gft.i3market.utilities.Utilities.readParametersFromProperties(properties);

			//
			// READ CONSTANTS PARAMETERS
			//
			logger.debug("Reading configured constants");
			String[] constantsArray = com.gft.i3market.utilities.Utilities.readConstantFromProperties(properties);

			//
			// FORMULA PARAMETERS
			//
			logger.debug("Reading Parameters from properties");
			this.formulaArguments = new HashMap<String, FormulaArgument>();

			for (int i = 0; i < parametersArray.length; i++) {
				String parameterName = parametersArray[i];

				FormulaArgument formulaArgument = new FormulaArgument();
				formulaArgument.setName(parameterName);

				/* (items in propery file example)
				 * Parameter_p1_description=Parametro 1 
				 * Parameter_p1_required=true
				 * Parameter_p1_defaultvalue=1
				 */

				String parameterDescriptionKey = "Parameter_" + parameterName + "_description";
				if (properties.containsKey(parameterDescriptionKey)) {
					formulaArgument.setDescription((String) properties.get(parameterDescriptionKey));
				} else {
					throw new Exception("Property: " + parameterDescriptionKey + " not found in propery file");
				}

				String parameterRequiredKey = "Parameter_" + parameterName + "_required";
				if (properties.containsKey(parameterRequiredKey)) {
					String st = (String) properties.get(parameterRequiredKey);
					if (st.equalsIgnoreCase("true"))
						formulaArgument.setRequired(true);
					else
						formulaArgument.setRequired(false);
				} else {
					throw new Exception("Property: " + parameterRequiredKey + " not found in propery file");
				}

				if (!formulaArgument.isRequired()) {
					String parameterDefualtKey = "Parameter_" + parameterName + "_defaultvalue";
					if (properties.containsKey(parameterDefualtKey)) {
						String st = (String) properties.get(parameterDefualtKey);

						Double df = Double.parseDouble(st);
						formulaArgument.setDefaultValue(df);
					} else {
						throw new Exception("Property: " + parameterDefualtKey + " not found in propery file");
					}
				}

				this.formulaArguments.put(parameterName, formulaArgument);
			}

			//
			// FORMULA CONSTANTS
			//
			logger.debug("Reading Constants from properties");
			this.formulaConstants = new HashMap<String, FormulaConstant>();

			for (int i = 0; i < constantsArray.length; i++) {
				String parameterName = constantsArray[i];

				FormulaConstant formulaArgument = new FormulaConstant();
				formulaArgument.setName(parameterName);

				/* (items in propery file example)
				 * Constant_c1_description=Constante 1 
				 * Constant_c1_value=10
				 */

				String parameterDescriptionKey = "Constant_" + parameterName + "_description";
				if (properties.containsKey(parameterDescriptionKey)) {
					formulaArgument.setDescription((String) properties.get(parameterDescriptionKey));
				} else {
					throw new Exception("Constant: " + parameterDescriptionKey + " not found in propery file");
				}

				String parameterDefualtKey = "Constant_" + parameterName + "_value";
				if (properties.containsKey(parameterDefualtKey)) {
					String st = (String) properties.get(parameterDefualtKey);

					Double df = Double.parseDouble(st);
					formulaArgument.setValue(df);
				} else {
					throw new Exception("Constant: " + parameterDefualtKey + " not found in propery file");
				}

				this.formulaConstants.put(parameterName, formulaArgument);
			}
		} catch (Exception ex) {
			logger.error("Error configuring: ", ex);
			throw ex;
		}
	}

}
