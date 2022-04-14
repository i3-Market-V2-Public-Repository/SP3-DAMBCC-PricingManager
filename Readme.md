## Pricing Manager

Pricing Manager is a web micro-service for configure and eval price and cost for machine learning data.
The service has been developed with the Java 11 language and the support of the Spring Boot framework (Version: 2.6.5.RELEASE).
It adopts the REST approach as a standard protocol for displaying services.
The service APIs are logically divided into two subsets:

Pricing management
Cost management

Pricing management:
This service is useful to calculate the price of some data based on a configured formula.
Sicne teh price formula has been configured the service can eval the price of data.

Cost management:
This service is useful to calculate the cost thet depends by price and fee.

## Services Documentation

### Price manager:

#### setformulawithdefaultconfiguration
This service can be used for set a formula using default formula parameters and constant values.
Query parameter:
-formula
Example:
formula=f(p1,p2,c1)=p1*p2+c1&contantslist=c1&parameterslist=p1,p2"
See the formula format with f("parameters and constants list")=math formula
Return parameter:
- String ("OK" value in case of operation success, "KO" value in case of error)

#### setformulaconstant
This service can be used to put the value of a formula constant
Query parameter:
-name
-value
-description
Example:
name: c1
value: 1
description: c1 description
Return parameter:
- String ("OK" value in case of operation success, "KO" value in case of error)

#### setformulaparameter
This service can be used to put the value of a formula parameter
Query parameter:
-name
-required
-defaultvalue
-description
Example:
name: p1
required: false
defaultvalue: 2
description: p2 description
Return parameter:
- String ("OK" value in case of operation success, "KO" value in case of error)

#### getformulajsonconfiguration
This service can be used to retrieve fornula, parameters and constants
Query parameter:
-none
Return parameter:
- JSON
The configuratrion is in JSON format like this:
*`{
  "formula": "f(b1,b2,b3,b4,b5,b6,b7,b8,b9,credibilityoftheseller,ageofdata,accuracyofdata,volumeofdata,costofcollectingandstorage,riskofprivacyviolations,exclusivityofaccess,rawdatavsprocesseddata,levelofownership)=b1*credibilityoftheseller+b2*ageofdata+b3*accuracyofdata+b4*volumeofdata+b5*costofcollectingandstorage+b6*riskofprivacyviolations+b7*exclusivityofaccess+b8*rawdatavsprocesseddata+b9*levelofownership",
  "formulaConstantConfiguration": [
    {
      "name": "b1",
      "description": "b1",
      "value": "1"
    },
    {
      "name": "b2",
      "description": "b2",
      "value": "1"
    },
    {
      "name": "b3",
      "description": "b3",
      "value": "1"
    },
    {
      "name": "b4",
      "description": "b4",
      "value": "1"
    },
    {
      "name": "b5",
      "description": "b5",
      "value": "1"
    },
    {
      "name": "b6",
      "description": "b6",
      "value": "1"
    },
    {
      "name": "b7",
      "description": "b7",
      "value": "1"
    },
    {
      "name": "b8",
      "description": "b8",
      "value": "1"
    },
    {
      "name": "b9",
      "description": "b8",
      "value": "1"
    }
  ],
  "formulaParameterConfiguration": [
    {
      "name": "credibilityoftheseller",
      "description": "Credibility of the Seller",
      "required": "false",
      "defaultvalue": "1"
    },
    {
      "name": "ageofdata",
      "description": "Age of Data",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "accuracyofdata",
      "description": "Accuracy of Data",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "volumeofdata",
      "description": "Volume of Data",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "costofcollectingandstorage",
      "description": "Cost of collecting and storage",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "riskofprivacyviolations",
      "description": "Risk of privacy violations",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "exclusivityofaccess",
      "description": "Exclusivity of access",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "rawdatavsprocesseddata",
      "description": "Raw data vs processed data",
      "required": "true",
      "defaultvalue": "1"
    },
    {
      "name": "levelofownership",
      "description": "Level of ownership",
      "required": "true",
      "defaultvalue": "1"
    }
  ]
}`*

#### setformulajsonconfiguration
This service can be used to set fornula, parameters and constants like as getformulajsonconfiguration service

Query parameter:
- JSON
Return parameter:
- String ("OK" value in case of operation success, "KO" value in case of error)

JSON format is the same as getformulajsonconfiguration service

#### getprice
This service can be used to get the price of data

Query parameter:
- parameters of the formula
Return parameter:
- the price
Example
input parameter: b1=1&b2=1&b3=1&b4=1&b5=1&b6=1&b7=1&b8=1&b9=1&

### Cost manager:

#### setfee
This service can be used for set the fee for eval cost by price.
Query parameter:
-fee
Return parameter:
- String ("OK" value in case of operation success, "KO" value in case of error)
Example:10 (in %)

#### getcost
This service can be used for get the cost by price.
Query parameter:
-price
Return parameter:
- String (the calculated cost)
Example:100

## Installation Requirements
The installation requirements are:

Java 11
Gradle
Spring Boot

## Configuration
Smart Tool Box Generator configuration properties are configured through the corresponding Spring profile settings. By default, dev profile is activated and the execution environment is configured in the src/main/resources/application-dev.properties file. The configuration for the production execution is defined in application.properties.

## Logging Configuration
The logging settings may be configured via standard Spring Boot properties, e.g. for the log level

log.level:

The project relies on the Logback configuration (see src/main/resources/logback.xml). The default configuration requires the log folder path defined with toolbox.log.folder property. (if the property is not set, application will use default value: WORKING_DIRECTORY/logs).

## Run with Docker:
Prerequisites Installed:
Docker
Docker-compose
docker-compose.yml
version: '3.7'
services:
    proxy:
        image: smartproxy
        ports:
        - 4500:8080
        environment:
          DFIL_SPRING_CONTEXT_PATH: /price-manager
          DFIL_SPRING_DATASOURCE_HOST: dbproxy
          DFIL_SPRING_DATASOURCE_USERNAME: dfil
        volumes: 
        - price-manager:/TODO

This docker-compose file run two services, price manager and the cost manager. With this configuration, the tool is exposed on port 4500 and with context path "price-manager".

## Commands:
In the project directory:

docker build -t price-manager .
docker-compose up

## APIs documentation - Swagger
The documentation of the exposed APIs, has been created with the Swagger framework (Version: 2.9.2) and can be displayed at:

http://localhost:8080/swagger-ui.html#/



