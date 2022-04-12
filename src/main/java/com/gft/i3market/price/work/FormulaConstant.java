package com.gft.i3market.price.work;

import java.util.Objects;

/**
 * 
 * @author A06F
 *
 */
public class FormulaConstant {

	public FormulaConstant() {

	}

	private String name = "";
	private String description = "";
	private Double value = 0.0;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FormulaConstant [name=" + name + ", description=" + description + ", value=" + value + "]";
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
		FormulaConstant other = (FormulaConstant) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(value, other.value);
	}
}
