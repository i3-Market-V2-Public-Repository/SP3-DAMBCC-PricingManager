package com.gft.test.jackson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ParserTest {

	public static void main(String[] args) {

		try 
		{
			//
			// USED ONLY FOR SANDBOX
			//
			
			// BASE SERIALIZING
			
			ObjectMapper objectMapper = new ObjectMapper();
			Car car = new Car("Red","Pandino");

			String carAsString = objectMapper.writeValueAsString(car);
			
			System.out.println("Car "+carAsString);
			
			// BASE DESERIALIZING
			
			String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
			Car car1 = objectMapper.readValue(json, Car.class);	
			
			System.out.println("Car "+car1);
			
			// BASE DESERIALIZING ARRAY
			
			String jsonCarArray =  "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
			List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
					
			System.out.println("Cars "+listCar);
			
			// BASE DESERIALIZING MAPS 
			
			String json1 = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
			Map<String, Object> map = objectMapper.readValue(json1, new TypeReference<Map<String,Object>>(){});
			
			System.out.println("Cars "+map);
			
			// CUSTOM NEW PROPERTIES
			
			String jsonString = "{ \"color\" : \"Black\", \"type\" : \"BMW\", \"year\" : \"2022\" }";
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			car = objectMapper.readValue(jsonString, Car.class);

			JsonNode jsonNodeRoot = objectMapper.readTree(jsonString);
			JsonNode jsonNodeYear = jsonNodeRoot.get("year");
			String year = jsonNodeYear.asText();
			System.out.println("year "+year);
			
			// CUSTOM CLASS SERIALIZATION
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module =  new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
			module.addSerializer(Car.class, new CustomCarSerializer());
			mapper.registerModule(module);
			Car car2 = new Car("yellow", "renault");
			String carJson = mapper.writeValueAsString(car2);
			
			System.out.println("Car custom serialyzing "+carJson);
			
			// CUSTOM CLASS DESERIALIZATION
			String json2 = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
			ObjectMapper mapper1 = new ObjectMapper();
			SimpleModule module1 =new SimpleModule("CustomCarDeserializer", new Version(1, 0, 0, null, null, null));
			module1.addDeserializer(Car.class, new CustomCarDeserializer());
			mapper1.registerModule(module1);
			Car car3 = mapper1.readValue(json2, Car.class);
			
			System.out.println("Car custom deserialyzing "+car3);
			
			// DATE
			objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			objectMapper.setDateFormat(df);
			String jsonStringDate = "{ \"color\" : \"Black\", \"type\" : \"BMW\", \"year\" : \"2022\",\"datePurchased\" : \"2022-03-12 14:32\"\"  }";
			carAsString = objectMapper.writeValueAsString(jsonStringDate);
	
			System.out.println("Car date "+carAsString);
			
			// ARRAYS
			jsonCarArray = 
					  "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
			objectMapper = new ObjectMapper();
			List<Car> listCar1 = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
			
			System.out.println("Car list "+listCar1);
			
			// ANNOTATIONS
			
			CarAnnotated carAnnotated = new CarAnnotated("Red","Pandino");

		    String result = new ObjectMapper().writeValueAsString(carAnnotated);
		    System.out.println("Car Anotated "+result);
		} 
		catch (JsonProcessingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
