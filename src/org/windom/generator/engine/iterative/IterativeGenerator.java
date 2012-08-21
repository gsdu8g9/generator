package org.windom.generator.engine.iterative;

import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.engine.common.AbstractGenerator;
import org.windom.generator.engine.common.GeneratorContext;

public class IterativeGenerator extends AbstractGenerator<IterativeNodeInstance> {

	public IterativeGenerator(Definition definition, Random rng) {
		super(definition, rng);
	}
	
	public IterativeGenerator(Definition definition) {
		super(definition, new Random());
	}
	
	@Override
	protected IterativeNodeInstance generate(Node node,
			GeneratorContext<IterativeNodeInstance> ctx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
