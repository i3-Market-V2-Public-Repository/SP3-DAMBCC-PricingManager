package com.gft.i3market.parameters;

import java.util.Arrays;
import java.util.Objects;

public class FormulaConfig {

	public FormulaConfig()
	{
		
	}
	
	private String formula = "";
	private FormulaConstantConfiguration[] formulaConstantConfiguration;
	private FormulaParameterConfiguration[] FormulaParameterConfiguration;
	
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public FormulaConstantConfiguration[] getFormulaConstantConfiguration() {
		return formulaConstantConfiguration;
	}
	public void setFormulaConstantConfiguration(FormulaConstantConfiguration[] formulaConstantConfiguration) {
		this.formulaConstantConfiguration = formulaConstantConfiguration;
	}
	public FormulaParameterConfiguration[] getFormulaParameterConfiguration() {
		return FormulaParameterConfiguration;
	}
	public void setFormulaParameterConfiguration(FormulaParameterConfiguration[] formulaParameterConfiguration) {
		FormulaParameterConfiguration = formulaParameterConfiguration;
	}
	@Override
	public String toString() {
		return "FormulaConfig [formula=" + formula + ", formulaConstantConfiguration="
				+ Arrays.toString(formulaConstantConfiguration) + ", FormulaParameterConfiguration="
				+ Arrays.toString(FormulaParameterConfiguration) + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(FormulaParameterConfiguration);
		result = prime * result + Arrays.hashCode(formulaConstantConfiguration);
		result = prime * result + Objects.hash(formula);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaConfig other = (FormulaConfig) obj;
		return Arrays.equals(FormulaParameterConfiguration, other.FormulaParameterConfiguration)
				&& Objects.equals(formula, other.formula)
				&& Arrays.equals(formulaConstantConfiguration, other.formulaConstantConfiguration);
	}
}
