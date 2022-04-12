package com.gft.test.jackson;

import java.util.Date;

public class Car {

    private String color;
    private String type;
    private String year;
    private Date datePurchased;
    
    public Car() {
		this.color = "";
		this.type = "";
		this.year = "";
	}
	
    
	public Car(String color,String type) {
		this.color = color;
		this.type = type;
	}
	
	public Car(String color,String type,String year) {
		this.color = color;
		this.type = type;
		this.year = year;
	}
	
	public Car(String color,String type,String year, Date datePurchased) {
		this.color = color;
		this.type = type;
		this.year = year;
		this.datePurchased = datePurchased;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	

	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	@Override
	public String toString() {
		return "Car [color=" + color + ", type=" + type + ", year=" + year + "]";
	}




}
