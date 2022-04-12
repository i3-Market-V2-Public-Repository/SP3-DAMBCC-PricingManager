package com.gft.i3market.price.work;

import java.util.Objects;

/**
 * 
 * @author A06F
 *
 */
public class FormulaArgument {

	public FormulaArgument() {

	}

	String name = "";
	String description = "";
	boolean required = false;
	Double defaultValue = 0.0;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FormulaArgument [name=" + name + ", description=" + description + ", required=" + required
				+ ", defaultValue=" + defaultValue + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(defaultValue, description, name, required);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaArgument other = (FormulaArgument) obj;
		return Objects.equals(defaultValue, other.defaultValue) && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name) && required == other.required;
	}

}
