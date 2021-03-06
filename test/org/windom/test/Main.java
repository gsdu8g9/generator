package org.windom.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Definition;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.TreeInstance;
import org.windom.generator.engine.iterative.IterativeGenerator;
import org.windom.generator.input.plain.PlainInput;
import org.windom.generator.postprocessor.PostProcessor;
import org.windom.generator.postprocessor.impl.ExpansionPostProcessor;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			Definition d = new PlainInput("test.txt").read();
			//System.out.println(d.dump());
			Generator g = new IterativeGenerator(d);
			PostProcessor p = new ExpansionPostProcessor();
			TreeInstance ti = g.generate();
			System.out.println(p.process(ti));
		} catch (Exception e) {
			log.error("Error testing", e);
		}
	}
	
}
