package org.windom.generator.input.plain;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Definition;
import org.windom.generator.input.Input;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.impl.BuilderImpl;
import org.windom.generator.input.plain.impl.ParserImpl;
import org.windom.generator.input.plain.impl.ScannerImpl;
import org.windom.generator.util.Utils;

public class PlainInput implements Input {

	private static final Logger log = LoggerFactory.getLogger(PlainInput.class);
	
	private final String filePath;
	
	public PlainInput(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public Definition read() throws InputException {
		Builder builder = new BuilderImpl();
		read(builder);
		return builder.build();
	}

	public void read(Builder builder) throws InputException {
		InputStream in = null;
		try {
			log.info("Reading rules from: {}", filePath);
			in = Utils.getClassPathResource(filePath);
			if (in == null) {
				throw new InputException("Resource not found: " + filePath);
			}
			Scanner scanner = new ScannerImpl(in);
			Parser parser = new ParserImpl(scanner, builder);
			parser.parse();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error("Error closing file", e);
			}
		}
	}
	
}
