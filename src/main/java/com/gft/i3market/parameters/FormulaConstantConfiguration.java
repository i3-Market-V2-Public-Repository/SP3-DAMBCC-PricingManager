package com.gft.i3market.parameters;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author A06F
 *
 */
public class FormulaConstantConfiguration {
	private String name;
	private String description;
	private String value;

	private static final Logger logger = LogManager.getLogger(FormulaConstantConfiguration.class);

	public FormulaConstantConfiguration() {

	}

	public boolean validate() throws Exception {
		if (this.getName().equalsIgnoreCase("")) {
			logger.error("No name provided");
			throw new Exception("No name provided");
		}

		if (this.getValue().equalsIgnoreCase("")) {
			logger.error("No value provided");
			throw new Exception("No value provided");
		} 
		
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FormulaConstantConfiguration [name=" + name + ", description=" + description + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaConstantConfiguration other = (FormulaConstantConfiguration) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(value, other.value);
	}

}