package org.windom.generator.engine.impl;

import java.util.ArrayList;
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

public abstract class AbstractGenerator implements Generator {

	protected final Definition definition;
	protected final Random rng;
	
	public AbstractGenerator(Definition definition, Random rng) {
		this.definition = definition;
		this.rng = rng;
	}
	
	@Override
	public TreeInstance generate() {
		return new TreeInstance(generate(definition.getStart()));
	}
	
	protected abstract boolean isApplicableRule(Rule rule);
	
	protected abstract Rule chooseRule(List<Rule> rules);
	
	protected NodeInstance generate(Node node) {
		if (node instanceof Terminal) {
			return new NodeInstance(node, null);
		} else {
			List<Rule> rules = getApplicableRules((Nonterminal) node);
			Rule rule = chooseRule(rules);
			RuleInstance ruleInstance = new RuleInstance(rule);
			for (Node rightNode : rule.getRight()) {
				ruleInstance.getNodeInstances().add(generate(rightNode));
			}
			return new NodeInstance(node, ruleInstance);
		}
	}
	
	private List<Rule> getApplicableRules(Nonterminal node) {
		List<Rule> applicableRules = new ArrayList<Rule>(node.getRules().size());
		for (Rule rule : node.getRules()) {
			if (isApplicableRule(rule)) {
				applicableRules.add(rule);
			}
		}
		return applicableRules;
	}
	
}
