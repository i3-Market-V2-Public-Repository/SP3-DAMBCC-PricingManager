
  

<!---

  

# Copyright 2020-2022 i3-MARKET Consortium:

  

#

  

# ATHENS UNIVERSITY OF ECONOMICS AND BUSINESS - RESEARCH CENTER

  

# ATOS SPAIN SA

  

# EUROPEAN DIGITAL SME ALLIANCE

  

# GFT ITALIA SRL

  

# GUARDTIME OU

  

# HOP UBIQUITOUS SL

  

# IBM RESEARCH GMBH

  

# IDEMIA FRANCE

  

# SIEMENS AKTIENGESELLSCHAFT

  

# SIEMENS SRL

  

# TELESTO TECHNOLOGIES PLIROFORIKIS KAI EPIKOINONION EPE

  

# UNIVERSITAT POLITECNICA DE CATALUNYA

  

# UNPARALLEL INNOVATION LDA

  

#

  

# Licensed under the Apache License, Version 2.0 (the "License");

  

# you may not use this file except in compliance with the License.

  

# You may obtain a copy of the License at

  

#

  

# http://www.apache.org/licenses/LICENSE-2.0

  

#

  

# Unless required by applicable law or agreed to in writing, software

  

# distributed under the License is distributed on an "AS IS" BASIS,

  

# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

  

# See the License for the specific language governing permissions and

  

# limitations under the License.

  

#

  

-->


# Pricing Manager

Pricing Manager is a web micro-service for configure and evaluate the price and the cost of data.
The service APIs are logically divided into two subsets:

**Pricing management:**

This service allows you to calculate the price of some data on the basis of a preconfigured formula. You can manage the formula and customize parameters and constants.

**Cost management:** 

This service can be used to calculate the fee of some data which depends on the price of the data and the percentage of the tariff. 

## Swagger documentation


The OAS documentation can be accessed [here](http://95.211.3.244:8181/SdkRefImpl/#/common-services:_pricingManager).

## Endpoints
 

### Price manager:

#### setformulawithdefaultconfiguration:

This service can be used for set a formula using default formula parameters and constant values.
 
Body parameter: 

- formula Example:

`formula=f(p1,p2,c1)=p1*p2+c1&contantslist=c1&parameterslist=p1,p2"`

The formula format is: f("parameters and constants list")=math formula

Return parameter: 

- String ("OK" value in case of operation success, "KO" value in case of error) 

#### setformulaconstant:  

This service can be used to put the value of a formula constant.

Body parameter:

- `{ "name":"string", "description":"string", "value":"string" }`  

Return parameter:

- String ("OK" value in case of operation success, "KO" value in case of error)

#### setformulaparameter: 

This service can be used to put the value of a formula parameter.

Body parameter:

- `{ "name":"string", "description":"string", "required":"string", "defaultvalue":"string" } `  

Return parameter:

- String ("OK" value in case of operation success, "KO" value in case of error) 

#### getformulajsonconfiguration:

This service can be used to retrieve the configured formula, parameters and constants.

The service has been preconfigured with the formula defined and analyzed by AUEB.

Return parameter:

- The formula configuration in JSON format  

#### setformulajsonconfiguration:

This service can be used to set formula, parameters and constants

Query parameter:  

- The input param is a string that represent the formula configuration in JSON format:

Example (The formula defined from AUEB in JSON format):

```json
{
"formula":"f(B1,B2,B3,B4,B5,CostOfCollecting,EstimatedValue,DataCompleteness,DataAccuracy,UniqueEntries,Rarity)=(CostOfCollecting+EstimatedValue*B1)*(1-(1-DataCompleteness/5)* B2-(1-DataAccuracy/5)*B3-(1-UniqueEntries/5)*B4-(1-Rarity/5)*B5)",
   "formulaConstantConfiguration":[
      {
         "name":"B1",
         "description":"Final Parameter for data value",
         "value":"0.05"
      },
      {
         "name":"B2",
         "description":"Data Completeness",
         "value":"0.3"
      },
      {
         "name":"B3",
         "description":"Data Accuracy and Validity",
         "value":"0.3"
      },
      {
         "name":"B4",
         "description":"Unique entries-values",
         "value":"0.1"
      },
      {
         "name":"B5",
         "description":"Rarity-Scarceness",
         "value":"1"
      }
   ],
   "formulaParameterConfiguration":[
      {
         "name":"CostOfCollecting",
         "description":"Cost of collecting, storing and analysis (if relevant)",
         "required":"true",
         "defaultvalue":"0"
      },
      {
         "name":"EstimatedValue",
         "description":"Estimated  value for the consumer ",
         "required":"true",
         "defaultvalue":"0"
      },
      {
         "name":"DataCompleteness",
         "description":"Data Completeness: The data is complete",
         "required":"true",
         "defaultvalue":"0"
      },
      {
         "name":"DataAccuracy",
         "description":"Data Accuracy and Validity: The data is accurate",
         "required":"true",
         "defaultvalue":"0"
      },
      {
         "name":"UniqueEntries",
         "description":"Unique entries-values: The are no duplicates in the dataset",
         "required":"true",
         "defaultvalue":"0"
      },
      {
         "name":"Rarity",
         "description":"Rarity-Scarceness: The data is rare",
         "required":"true",
         "defaultvalue":"0"
      }
   ]
}

```

Return parameter:

- String ("OK" value in case of operation success, "KO" value in case of error) 

#### getprice:

This service can be used to get the price of data: 

Query parameter: 

- String that contains the characteristic parameters of the data in JSON format.

- The characteristics params of the data are inserted as *parameters* in the formula, those used in the example below have already been included in the preconfigured formula within the service and are described in the slides provided by AUEB.

  

Example:

```json

{
   "CostOfCollecting":"20",
   "EstimatedValue":"1000",
   "DataCompleteness":"5",
   "DataAccuracy":"5",
   "UniqueEntries":"5",
   "Rarity":"5"
}

```
Return parameter: 

- the calciulated price as a number

### Cost manager:

#### setfee:

This service can be used for set the fee to evaluate the cost of data.

Query parameter:

- The fee percentage

Example: 10 (if wanted percentage is 10%)

Return parameter:

- String ("OK" value in case of operation success, "KO" value in case of error)

#### getcost:

This service can be used to get the fee of some data by price.

Query parameter:

- The price of the data

Return parameter:

- The calculated fee

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



