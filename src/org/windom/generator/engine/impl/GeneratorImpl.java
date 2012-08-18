package org.windom.generator.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.GeneratorException;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.util.IndentedLogger;

public class GeneratorImpl implements Generator {

	private static final IndentedLogger log = new IndentedLogger(GeneratorImpl.class, "  ");
	
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
	public TreeInstance generate() throws GeneratorException {
		GeneratorContext ctx = new GeneratorContext();
		NodeInstance startInstance = generate(
				definition.getStart(),
				ctx);
		log.info("succeeded rules: {} failed rules: {}", 
				ctx.getStats().getSucceededRules(),
				ctx.getStats().getFailedRules());
		if (startInstance == null) {
			throw new GeneratorException("Failed to generate anything");
		}
		return new TreeInstance(startInstance);
	}
	
	private NodeInstance generate(Node node, GeneratorContext ctx) {
		if (node instanceof Terminal) {
			return new NodeInstance(node);
		} if (node instanceof Annotated) {
			return generate((Annotated) node, ctx);
		} else if (node instanceof Symbol) {
			log.debug("begin-generate {}", node);
			log.indent();
			try {
				List<Rule> rules = new ArrayList<Rule>(node.symbol().getRules());
				while (!rules.isEmpty()) {					
					Rule rule = chooseRule(rules);
					GeneratorContext branchCtx = ctx.branch();
					log.indent();
					NodeInstance nodeInstance = generate(rule, branchCtx);
					log.unindent();
					if (nodeInstance != null) {
						ctx.getStats().succeededRule();
						ctx.merge(branchCtx, true);
						generate(
							new Annotated(Annotation.ADD_TAG, node.symbol()),
							ctx);
						return nodeInstance;
					} else {
						ctx.getStats().failedRule();
						ctx.merge(branchCtx, false);
						rules.remove(rule);
					}
				}
				log.debug("no rules to apply");
				return null;
			} finally {
				log.unindent();
				log.debug("end-generate {}", node);
			}
		} else {
			throw new RuntimeException("Unsupported node type: " + node.getClass());
		}
	}
	
	private NodeInstance generate(Annotated annotated, GeneratorContext ctx) {
		switch (annotated.getAnnotation()) {
		case PERM: {
			NodeInstance nodeInstance = ctx.getPermNodeInstance(annotated);
			if (nodeInstance == null) {
				log.debug("{} is not bound", annotated);
				nodeInstance = generate(annotated.getNonterminal(), ctx);
				ctx.setPermNodeInstance(annotated, nodeInstance);					
			} else {
				log.debug("{} is already bound", annotated);
			}
			return nodeInstance;
		} 
		case ADD_TAG: { 
			ctx.addTag(annotated.symbol().getName());
			log.debug("{} applied", annotated);
			return new NodeInstance(annotated);
		}
		case DEL_TAG: {
			ctx.delTag(annotated.symbol().getName());
			log.debug("{} applied", annotated);
			return new NodeInstance(annotated);
		}
		case CHECK_TAG: {
			boolean result = ctx.checkTag(annotated.symbol().getName());
			log.debug("{} result: {}", annotated, result);
			return result ? new NodeInstance(annotated) : null;
		}
		default:
			throw new RuntimeException("Unsupported symbol annotation: " + annotated.getAnnotation());
		}
	}
	
	private NodeInstance generate(Rule rule, GeneratorContext ctx) {
		RuleInstance ruleInstance = new RuleInstance(rule);
		for (Node rightNode : rule.getRight()) {
			NodeInstance rightNodeInstance = generate(rightNode, ctx);
			if (rightNodeInstance != null) {
				ruleInstance.getNodeInstances().add(rightNodeInstance);
			} else {
				log.debug("failed {}", rightNode);
				return null;
			}
		}
		return new NodeInstance(rule.getLeft(), ruleInstance);
	}
	
	private Rule chooseRule(List<Rule> rules) {
		log.debug("applicable-rules: {}", rules);
		Rule rule = rules.get(rng.nextInt(rules.size()));
		log.debug("chosen-rule: {}", rule);
		return rule;
	}
	
}
