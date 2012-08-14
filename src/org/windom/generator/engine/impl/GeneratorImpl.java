package org.windom.generator.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.windom.generator.definition.AnnotatedNonterminal;
import org.windom.generator.definition.Annotation;
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

	protected final Definition definition;
	protected final Random rng;
	
	public GeneratorImpl(Definition definition, Random rng) {
		this.definition = definition;
		this.rng = rng;
	}
	
	public GeneratorImpl(Definition definition) {
		this(definition, new Random());
	}
	
	@Override
	public TreeInstance generate() {
		return new TreeInstance(generate(
				definition.getStart(),
				new GeneratorContext()));
	}
	
	private NodeInstance generate(Node node, GeneratorContext ctx) {
		if (node instanceof Terminal) {
			return new NodeInstance(node, null);
		} if (node instanceof AnnotatedNonterminal) {
			AnnotatedNonterminal anode = (AnnotatedNonterminal) node;
			if (anode.getAnnotation() == Annotation.PERM) {
				NodeInstance nodeInstance = ctx.getPermNodeInstance(anode);
				if (nodeInstance == null) {
					nodeInstance = generate(anode.getNonterminal(), ctx);
					ctx.setPermNodeInstance(anode, nodeInstance);
				}
				return nodeInstance;
			} else {
				throw new RuntimeException("Unsupported node annotation: " + anode.getAnnotation());
			}
		} else if (node instanceof Nonterminal) {
			List<Rule> rules = getApplicableRules((Nonterminal) node);
			Rule rule = chooseRule(rules);
			RuleInstance ruleInstance = new RuleInstance(rule);
			for (Node rightNode : rule.getRight()) {
				ruleInstance.getNodeInstances().add(generate(rightNode, ctx));
			}
			return new NodeInstance(node, ruleInstance);
		} else {
			throw new RuntimeException("Unsupported node type: " + node.getClass());
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
	
	private boolean isApplicableRule(Rule rule) {
		return true;
	}
	
	private Rule chooseRule(List<Rule> rules) {
		return rules.get(rng.nextInt(rules.size()));
	}
	
}
