package com.gft.i3market.parameters;

public class FormulaWithConfiguration {
	String formula = "";
	String contantslist = "";
	String parameterslist = "";
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getContantslist() {
		return contantslist;
	}
	public void setContantslist(String contantslist) {
		this.contantslist = contantslist;
	}
	public String getParameterslist() {
		return parameterslist;
	}
	public void setParameterslist(String parameterslist) {
		this.parameterslist = parameterslist;
	}
	@Override
	public String toString() {
		return "FormulaWithConfiguration [formula=" + formula + ", contantslist=" + contantslist + ", parameterslist="
				+ parameterslist + "]";
	}
	
	
}
