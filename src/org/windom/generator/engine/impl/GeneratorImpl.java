package org.windom.generator.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.windom.generator.definition.AnnotatedNonterminal;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.util.IndentedLogger;

public class GeneratorImpl implements Generator {

	private static final IndentedLogger log = new IndentedLogger(GeneratorImpl.class, "..");
	
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
			return new NodeInstance(node);
		} if (node instanceof AnnotatedNonterminal) {
			AnnotatedNonterminal anode = (AnnotatedNonterminal) node;
			switch (anode.getAnnotation()) {
			case PERM: {
				NodeInstance nodeInstance = ctx.getPermNodeInstance(anode);
				if (nodeInstance == null) {
					log.debug("{} is not bound", anode);
					nodeInstance = generate(anode.getNonterminal(), ctx);
					ctx.setPermNodeInstance(anode, nodeInstance);					
				} else {
					log.debug("{} is already bound", anode);
				}
				return nodeInstance;
			} 
			case ADD_TAG: { 
				ctx.addTag(anode.getNonterminal().getName());
				log.debug("{} applied", anode);
				return new NodeInstance(anode);
			}
			case DEL_TAG: {
				ctx.delTag(anode.getNonterminal().getName());
				log.debug("{} applied", anode);
				return new NodeInstance(anode);
			}
			default:
				throw new RuntimeException("Unsupported node annotation: " + anode.getAnnotation());
			}
		} else if (node instanceof Nonterminal) {
			log.debug("begin-generate {}", node);
			log.indent();
			try {
				List<Rule> rules = getApplicableRules((Nonterminal) node);
				Rule rule = chooseRule(rules);
				RuleInstance ruleInstance = new RuleInstance(rule);
				GeneratorContext branchCtx = ctx.branch();
				for (Node rightNode : rule.getRight()) {
					ruleInstance.getNodeInstances().add(generate(rightNode, branchCtx));
				}
				ctx.merge(branchCtx);
				return new NodeInstance(node, ruleInstance);
			} finally {
				log.unindent();
				log.debug("end-generate {}", node);
			}
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
		log.debug("applicable-rules: {}", applicableRules);
		return applicableRules;
	}
	
	private boolean isApplicableRule(Rule rule) {
		return true;
	}
	
	private Rule chooseRule(List<Rule> rules) {
		Rule rule = rules.get(rng.nextInt(rules.size()));
		log.debug("chosen-rule: {}", rule);
		return rule;
	}
	
}
