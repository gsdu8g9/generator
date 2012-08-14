package org.windom.generator.engine.impl;

import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Rule;

public class GeneratorImpl extends AbstractGenerator {

	public GeneratorImpl(Definition definition, Random rng) {
		super(definition, rng);
	}
	
	public GeneratorImpl(Definition definition) {
		super(definition, new Random());
	}
	
	@Override
	protected boolean isApplicableRule(Rule rule) {
		return true;
	}
	
	@Override
	protected Rule chooseRule(List<Rule> rules) {
		return rules.get(rng.nextInt(rules.size()));
	}
	
}
