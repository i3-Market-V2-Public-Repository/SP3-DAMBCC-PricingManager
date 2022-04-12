package com.gft.i3market.price.work;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;

/**
 * Formula worker
 * 
 * @author A06F
 *
 */
public class FormulaWorker {

	public FormulaWorker() {

	}

	private static final Logger logger = LogManager.getLogger(FormulaWorker.class);

	/**
	 * Evals value of formula using formula and parameters
	 * @param formulaConfiguration
	 * @param requestParameters
	 * @return
	 * @throws Exception
	 */
	public double calculateFormula(FormulaConfiguration formulaConfiguration, Map<String, String> requestParameters)
			throws Exception {
		double eval = 0;

		try {
			logger.info("Starting formula calculation");

			String formula = formulaConfiguration.getFormula();
			// Check formula syntax
			Function f = new Function(formula);

			boolean checkSyntax = f.checkSyntax();

			if (!checkSyntax) {
				throw new Exception("Bad formula syntax");
			}

			// eval formula
			Integer numArgs = f.getArgumentsNumber();
			for (int i = 0; i < numArgs; i++) {
				// check if any argumenti is present in constant configuration or in request parameters
				Argument argument = f.getArgument(i);

				String argumentName = argument.getArgumentName();

				logger.debug("Checking formula Argument: " + argumentName);

				// check presence in constants
				boolean exixtOnConstant = false;

				if (formulaConfiguration.getFormulaConstants().containsKey(argumentName)) {
					exixtOnConstant = true;
					Double avd = formulaConfiguration.getFormulaConstants().get(argumentName).getValue();
					logger.debug("Found in constant parameter by " + avd);
					argument.setArgumentValue(avd);
				}

				boolean exixtOnParameters = false;

				// check presence in request parameters
				// first check on constants to avoid overriding contant value by request parameters
				if (!exixtOnConstant) {
					if (requestParameters.containsKey(argumentName)) {
						exixtOnParameters = true;
						String rval = requestParameters.get(argumentName);
						logger.debug("Found in request parameter by " + rval);
						Double avd = Double.valueOf(rval);
						argument.setArgumentValue(avd);
					} else {
						if (formulaConfiguration.getFormulaArguments().containsKey(argumentName)) {
							boolean required = formulaConfiguration.getFormulaArguments().get(argumentName).required;

							if (required) {
								throw new Exception("No parameter for required: " + argumentName);
							} else {
								Double avd = formulaConfiguration.getFormulaArguments().get(argumentName)
										.getDefaultValue();
								logger.debug(
										"Not found in request parameter but is not required and use defautl by " + avd);
								exixtOnParameters = true;
								argument.setArgumentValue(avd);
							}
						}
					}

					if (exixtOnParameters || exixtOnConstant) {
						logger.debug("Argument is ok");
					} else {
						throw new Exception("No value found for: " + argumentName);
					}

				}
			}

			logger.info("Starting formula evaluation");
			
			// finally eval formula having all arguments
			try {
				eval = f.calculate();
			} catch (Exception ex) {
				throw ex;
			}

			logger.info("Finish formula calculation");
		} catch (Exception ex) {
			logger.error("Error calculation formula: ", ex);
			throw ex;
		}

		return eval;
	}
}
