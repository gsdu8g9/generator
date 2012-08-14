package org.windom.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Definition;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.impl.GeneratorImpl;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.PlainInput;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			Definition d = new PlainInput("test.txt").read();			
			Generator g = new GeneratorImpl(d);
			System.out.println(g.generate().getLimit());
		} catch (InputException e) {
			log.error("Error reading input",e);
		}
	}
	
}
