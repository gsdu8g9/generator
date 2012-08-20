package org.windom.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.util.Utils;

public class Config {

	private static final Logger log = LoggerFactory.getLogger(Config.class);
	
	private static final String CONFIG_FILE = "generator.properties";
	private static final Properties properties; 
	
	static {
		properties = new Properties();
		InputStream propertiesIs = Utils.getClassPathResource(CONFIG_FILE);
		if (propertiesIs != null) {
			log.info("Loading config from: {}", CONFIG_FILE);
			try {
				properties.load(propertiesIs);
			} catch (IOException e) {
				log.error("Error reading config file", e);
			} finally {
				try {
					propertiesIs.close();
				} catch (IOException e) {
					log.error("Error closing config file", e);
				}
			}
		} else {
			log.info("No config file present");
		}
	}
	
	public static boolean getFlag(String name, boolean defValue) {
		return Utils.parseBool(properties.getProperty(name), defValue);
	}
	
}
