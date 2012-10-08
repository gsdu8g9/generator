package org.windom.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndentedLogger {

	private final Logger logger;
	private final String indentUnit;
	private String indentString;
	
	public IndentedLogger(Class<?> clazz, String indentUnit) {
		this.logger = LoggerFactory.getLogger(clazz);
		this.indentUnit = indentUnit;
		this.indentString = "";
	}
	
	public IndentedLogger(Class<?> clazz) {
		this(clazz, "  ");
	}
	
	public void indent() {
		indentString += indentUnit;
	}
	
	public void unindent() {
		if (indentString.length() < indentUnit.length()) {
			throw new RuntimeException("Unbalanced indent()-unindent()'s");
		}
		indentString = indentString.substring(indentUnit.length());
	}
	
	public void info(String message, Object...args) {
		logger.info(indentString + message, args);
	}
	
	public void debug(String message, Object...args) {
		logger.debug(indentString + message, args);
	}
	
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
}
