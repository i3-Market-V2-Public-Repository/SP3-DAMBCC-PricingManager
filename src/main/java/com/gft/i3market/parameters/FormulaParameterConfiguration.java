package com.gft.i3market.parameters;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author A06F
 *
 */
public class FormulaParameterConfiguration {
	private String name;
	private String description;
	private String required;
	private String defaultvalue;

	private static final Logger logger = LogManager.getLogger(FormulaParameterConfiguration.class);

	public FormulaParameterConfiguration() {

	}

	public boolean validate() throws Exception {
		if (this.getName().equalsIgnoreCase("")) {
			logger.error("No name provided");
			throw new Exception("No name provided");
		}

		if (this.getRequired().equalsIgnoreCase("")) {
			logger.error("No required provided");
			throw new Exception("No required provided");
		} else {
			if (!this.getRequired().equalsIgnoreCase("true") && !this.getRequired().equalsIgnoreCase("false")) {
				logger.error("Bad required provided");
				throw new Exception("Bad required provided");
			} else {
				if (this.getRequired().equalsIgnoreCase("true")) {
					this.setRequired("true");
				} else {
					this.setRequired("false");

					if (this.getDefaultvalue().equalsIgnoreCase("")) {
						logger.error("No default value provided");
						throw new Exception("No default value provided");
					} else {
						try {
							Double.parseDouble(this.getDefaultvalue());
						} catch (Exception ee) {
							logger.error("Bad default value provided");
							throw new Exception("Bad default value provided");
						}
					}
				}
			}
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

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	@Override
	public String toString() {
		return "PutConstParams [name=" + name + ", description=" + description + ", required=" + required
				+ ", defaultvalue=" + defaultvalue + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(defaultvalue, description, name, required);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaParameterConfiguration other = (FormulaParameterConfiguration) obj;
		return Objects.equals(defaultvalue, other.defaultvalue) && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name) && Objects.equals(required, other.required);
	}
}