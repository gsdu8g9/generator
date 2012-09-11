package org.windom.generator.engine.recursive;

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
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.common.AbstractGenerator;
import org.windom.generator.engine.common.GeneratorContext;

public class RecursiveGenerator extends AbstractGenerator {
	
	public RecursiveGenerator(Definition definition, Random rng) {
		super(definition, rng);
	}
	
	public RecursiveGenerator(Definition definition) {
		super(definition, new Random());
	}
	
	@Override
	protected NodeInstance generate(Node node, GeneratorContext ctx) {
		if (node instanceof Terminal) {
			return new NodeInstance(node);
		} if (node instanceof Annotated) {
			return generate((Annotated) node, ctx);
		} else if (node instanceof Symbol) {
			log.debug("begin-generate {}", node);
			log.indent();
			try {
				List<Rule> rules = new ArrayList<Rule>(((Symbol) node).getRules());
				while (!rules.isEmpty()) {
					Rule rule = chooseAndRemoveRule(rules);
					GeneratorContext branchCtx = ctx.branch();
					log.indent();
					NodeInstance nodeInstance = generate(rule, branchCtx);
					log.unindent();
					if (nodeInstance != null) {
						ctx.getStats().succeededRule();
						ctx.merge(branchCtx, true);
						generate(
							new Annotated(Annotation.ADD_TAG, node),
							ctx);
						return nodeInstance;
					} else {
						ctx.getStats().failedRule();
						ctx.merge(branchCtx, false);
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
				nodeInstance = generate(annotated.getNode(), ctx);
				ctx.setPermNodeInstance(annotated, nodeInstance);					
			} else {
				log.debug("{} is already bound", annotated);
			}
			return nodeInstance;
		}
		case ADD_TAG: { 
			ctx.addTag(annotated.getNode().getName());
			log.debug("{} applied", annotated);
			return new NodeInstance(annotated);
		}
		case DEL_TAG: {
			ctx.delTag(annotated.getNode().getName());
			log.debug("{} applied", annotated);
			return new NodeInstance(annotated);
		}
		case CHECK_TAG: {
			boolean result = ctx.checkTag(annotated.getNode().getName());
			log.debug("{} result: {}", annotated, result);
			return result ? new NodeInstance(annotated) : null;
		}
		case SUCCEEDS:
		case FAILS: {
			GeneratorContext branchCtx = ctx.branch();
			log.debug("{} checking", annotated);
			log.indent();
			boolean result = (generate(annotated.getNode(), branchCtx) != null);
			if (annotated.getAnnotation() == Annotation.FAILS) result = !result;
			log.unindent();
			log.debug("{} result: {}", annotated, result);
			ctx.mergeStats(branchCtx, true);
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
	
}
