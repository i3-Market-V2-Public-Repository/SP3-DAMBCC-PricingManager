package com.gft.test.jackson;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "tipo", "colore" })
public class CarAnnotated {

	 @JsonAlias({ "colore", "copopolore" })
    private String color;
    private String type;
    private String year;
    private Date datePurchased;
    
    @JsonIgnore
    private String maffa;
    
    @JsonIgnoreType
    public static class Name {
        public String firstName;
        public String lastName;
    }
    
    public CarAnnotated() {
		this.color = "";
		this.type = "";
		this.year = "";
	}
	
    
	public CarAnnotated(String color,String type) {
		this.color = color;
		this.type = type;
	}
	
	public CarAnnotated(String color,String type,String year) {
		this.color = color;
		this.type = type;
		this.year = year;
	}
	
	public CarAnnotated(String color,String type,String year, Date datePurchased) {
		this.color = color;
		this.type = type;
		this.year = year;
		this.setDatePurchased(datePurchased);
	}
	
	@JsonGetter("colore")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	@JsonGetter("tipo")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@JsonGetter("anno")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@JsonGetter("acquisto")
	public Date getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}
	
	
	public String getMaffa() {
		return maffa;
	}


	public void setMaffa(String maffa) {
		this.maffa = maffa;
	}

	

	@Override
	public String toString() {
		return "Car [color=" + color + ", type=" + type + ", year=" + year + "]";
	}

}
