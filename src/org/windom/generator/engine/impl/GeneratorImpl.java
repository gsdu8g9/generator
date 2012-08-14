package org.windom.generator.engine.impl;

import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.TreeInstance;

public class GeneratorImpl implements Generator {

	private final Definition definition;
	private final Random rng;
	
	public GeneratorImpl(Definition definition, Random rng) {
		this.definition = definition;
		this.rng = rng;
	}
	
	public GeneratorImpl(Definition definition) {
		this(definition, new Random());
	}
	
	@Override
	public TreeInstance generate() {
		return new TreeInstance(generate(definition.getStart()));
	}
	
	protected NodeInstance generate(Node node) {
		if (node instanceof Terminal) {
			return new NodeInstance(node, null);
		} else {
			Rule rule = chooseRule((Nonterminal) node);
			RuleInstance ruleInstance = new RuleInstance(rule);
			for (Node rightNode : rule.getRight()) {
				ruleInstance.getNodeInstances().add(generate(rightNode));
			}
			return new NodeInstance(node, ruleInstance);
		}
	}
	
	protected Rule chooseRule(Nonterminal node) {
		List<Rule> rules = node.getRules();
		return rules.get(rng.nextInt(rules.size()));
	}
	
}
