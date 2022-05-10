package com.gft.i3market.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilities
 * 
 * @author A06F
 *
 */
public class Utilities {

	private static final Logger logger = LogManager.getLogger(Utilities.class);

	/**
	 * Read properties from property file
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> readProperties(String path) throws Exception {
		//
		// READ PROPERTY FROM RESOURCE
		//
		Map<String, String> properties = new HashMap<String, String>();
		try (InputStream input = new FileInputStream(path)) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			input.close();

			for (Iterator iterator = prop.keySet().iterator(); iterator.hasNext();) {
				String currKey = (String) iterator.next();
				String currVal = (String) prop.get(currKey);
				logger.debug("Found property: " + currKey + " -> " + currVal);
				properties.put(currKey, currVal);
			}

		} catch (Exception ex) {
			logger.error("Error getting propertiess: ", ex);
			throw ex;
		}

		return properties;
	}

	/**
	 * Log request parameters
	 * 
	 * @param parameters
	 * @throws Exception
	 */
	public static void logRequestParameters(Map<String, String> parameters) throws Exception {
		//
		// READ REQUEST PARAMETERS
		//
		logger.info("REQUEST PARAMETERS");
		int paramsCount = 0;
		try {
			for (Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
				paramsCount++;
				String currKey = (String) iterator.next();
				String currVal = (String) parameters.get(currKey);
				logger.info("Found parameter: " + currKey + " -> " + currVal);
			}
		} catch (Exception ex) {
			logger.error("Error getting parameters: ", ex);
			throw ex;
		}
		logger.info("END REQUEST PARAMETERS");
	}

	/**
	 * Read constants from property file
	 * 
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static String[] readConstantFromProperties(Map<String, String> properties) throws Exception {
		String[] constantsArray = null;
		try {
			// read costants
			logger.debug("Reading configured constants");

			if (properties.containsKey("Constants")) {
				String constants_list = (String) properties.get("Constants");
				constants_list = constants_list.trim();

				if (constants_list.equalsIgnoreCase("")) {
					logger.debug("no constants defined");
					constantsArray = new String[0];
				} else {
					if (constants_list.contains(";")) {
						logger.debug("multiple constants defined");
						constantsArray = constants_list.split(";");
					} else {
						logger.debug("one constants defined");
						constantsArray = new String[1];
						constantsArray[0] = constants_list;
					}
				}
			} else {
				throw new Exception("Constants not defined. Ad 'Constants' in config.properties");
			}
		} catch (Exception e) {
			throw e;

		}
		return constantsArray;
	}

	/**
	 * Read constants from property file
	 * 
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static String[] readParametersFromProperties(Map<String, String> properties) throws Exception {
		String[] parametersArray = null;
		try {
			logger.debug("Reading configured parameters");
			if (properties.containsKey("Parameters")) {
				String parameters_list = (String) properties.get("Parameters");
				parameters_list = parameters_list.trim();

				if (parameters_list.equalsIgnoreCase("")) {
					logger.debug("no parameters defined");
					parametersArray = new String[0];
				} else {
					if (parameters_list.contains(";")) {
						logger.debug("multiple parameters defined");
						parametersArray = parameters_list.split(";");
					} else {
						logger.debug("one parameter defined");
						parametersArray = new String[1];
						parametersArray[0] = parameters_list;
					}
				}
			} else {
				throw new Exception("Parameters not defined. Ad 'Parameters' in config.properties");
			}
		} catch (Exception e) {
			throw e;

		}
		return parametersArray;
	}

	/**
	 * Save to property to property file
	 * 
	 * @param propertiesMap
	 * @param comments
	 * @param destination
	 * @throws Exception
	 */
	public static void saveSortedPropertiesToFile(Map<String, String> propertiesMap, String comments, Path destination)
			throws Exception {
		Properties properties = new Properties();
		for (Iterator iterator = propertiesMap.keySet().iterator(); iterator.hasNext();) {
			String currKey = (String) iterator.next();
			String currVal = (String) propertiesMap.get(currKey);
			properties.put(currKey, currVal);
		}

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Storing it to output stream is the only way to make sure correct encoding is
			// used.
			properties.store(outputStream, comments);

			/*
			 * The encoding here shouldn't matter, since you are not going to modify the
			 * contents, and you are only going to split them to lines and reorder them. And
			 * Properties.store(OutputStream, String) should have translated unicode
			 * characters into (backslash)uXXXX anyway.
			 */
			String propertiesContentUnsorted = outputStream.toString("UTF-8");

			String propertiesContentSorted;
			try (BufferedReader bufferedReader = new BufferedReader(new StringReader(propertiesContentUnsorted))) {
				List<String> commentLines = new ArrayList<>();
				List<String> contentLines = new ArrayList<>();

				boolean commentSectionEnded = false;
				for (Iterator<String> it = bufferedReader.lines().iterator(); it.hasNext();) {
					String line = it.next();
					if (!commentSectionEnded) {
						if (line.startsWith("#")) {
							commentLines.add(line);
						} else {
							contentLines.add(line);
							commentSectionEnded = true;
						}
					} else {
						contentLines.add(line);
					}
				}
				// Sort on content lines only
				propertiesContentSorted = Stream.concat(commentLines.stream(), contentLines.stream().sorted())
						.collect(Collectors.joining(System.lineSeparator()));
			}

			// Just make sure you use the same encoding as above.
			Files.write(destination, propertiesContentSorted.getBytes(StandardCharsets.UTF_8));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Save to property to property file
	 * 
	 * @param properties
	 * @param comments
	 * @param destination
	 */
	public static void saveSortedPropertiesToFile(Properties properties, String comments, Path destination) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Storing it to output stream is the only way to make sure correct encoding is
			// used.
			properties.store(outputStream, comments);

			/*
			 * The encoding here shouldn't matter, since you are not going to modify the
			 * contents, and you are only going to split them to lines and reorder them. And
			 * Properties.store(OutputStream, String) should have translated unicode
			 * characters into (backslash)uXXXX anyway.
			 */
			String propertiesContentUnsorted = outputStream.toString("UTF-8");

			String propertiesContentSorted;
			try (BufferedReader bufferedReader = new BufferedReader(new StringReader(propertiesContentUnsorted))) {
				List<String> commentLines = new ArrayList<>();
				List<String> contentLines = new ArrayList<>();

				boolean commentSectionEnded = false;
				for (Iterator<String> it = bufferedReader.lines().iterator(); it.hasNext();) {
					String line = it.next();
					if (!commentSectionEnded) {
						if (line.startsWith("#")) {
							commentLines.add(line);
						} else {
							contentLines.add(line);
							commentSectionEnded = true;
						}
					} else {
						contentLines.add(line);
					}
				}
				// Sort on content lines only
				propertiesContentSorted = Stream.concat(commentLines.stream(), contentLines.stream().sorted())
						.collect(Collectors.joining(System.lineSeparator()));
			}

			// Just make sure you use the same encoding as above.
			Files.write(destination, propertiesContentSorted.getBytes(StandardCharsets.UTF_8));

		} catch (IOException e) {
			// Log it if necessary
		}
	}

	/**
	 * Ronud double with digit
	 * 
	 * @param src
	 * @param decimalPlaces
	 * @return
	 */
	public static Double round(Number src, int decimalPlaces) {

		return Optional.ofNullable(src).map(Number::doubleValue).map(BigDecimal::new)
				.map(dbl -> dbl.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)).map(BigDecimal::doubleValue)
				.orElse(null);
	}
}
