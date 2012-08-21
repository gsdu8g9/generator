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
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.common.AbstractGenerator;
import org.windom.generator.engine.common.GeneratorContext;

public class RecursiveGenerator extends AbstractGenerator<RecursiveNodeInstance> {
	
	public RecursiveGenerator(Definition definition, Random rng) {
		super(definition, rng);
	}
	
	public RecursiveGenerator(Definition definition) {
		super(definition, new Random());
	}
	
	@Override
	protected RecursiveNodeInstance generate(Node node, 
			GeneratorContext<RecursiveNodeInstance> ctx) {
		if (node instanceof Terminal) {
			return new RecursiveNodeInstance(node);
		} if (node instanceof Annotated) {
			return generate((Annotated) node, ctx);
		} else if (node instanceof Symbol) {
			log.debug("begin-generate {}", node);
			log.indent();
			try {
				List<Rule> rules = new ArrayList<Rule>(node.symbol().getRules());
				while (!rules.isEmpty()) {					
					Rule rule = chooseRule(rules);
					GeneratorContext<RecursiveNodeInstance> branchCtx = ctx.branch();
					log.indent();
					RecursiveNodeInstance nodeInstance = generate(rule, branchCtx);
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
	
	private RecursiveNodeInstance generate(Annotated annotated, 
			GeneratorContext<RecursiveNodeInstance> ctx) {
		switch (annotated.getAnnotation()) {
		case PERM: {
			RecursiveNodeInstance nodeInstance = ctx.getPermNodeInstance(annotated);
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
			return new RecursiveNodeInstance(annotated);
		}
		case DEL_TAG: {
			ctx.delTag(annotated.symbol().getName());
			log.debug("{} applied", annotated);
			return new RecursiveNodeInstance(annotated);
		}
		case CHECK_TAG: {
			boolean result = ctx.checkTag(annotated.symbol().getName());
			log.debug("{} result: {}", annotated, result);
			return result ? new RecursiveNodeInstance(annotated) : null;
		}
		case SUCCEEDS:
		case FAILS: {
			GeneratorContext<RecursiveNodeInstance> branchCtx = ctx.branch();
			log.debug("{} checking", annotated);
			log.indent();
			boolean result = (generate(annotated.getNonterminal(), branchCtx) != null);
			log.unindent();
			if (annotated.getAnnotation() == Annotation.FAILS) result = !result;
			log.debug("{} result: {}", annotated, result);
			ctx.mergeStats(branchCtx, true);
			return result ? new RecursiveNodeInstance(annotated) : null;
		}
		default:
			throw new RuntimeException("Unsupported symbol annotation: " + annotated.getAnnotation());
		}
	}
	
	private RecursiveNodeInstance generate(Rule rule, GeneratorContext<RecursiveNodeInstance> ctx) {
		RuleInstance<RecursiveNodeInstance> ruleInstance = 
				new RuleInstance<RecursiveNodeInstance>(rule);
		for (Node rightNode : rule.getRight()) {
			RecursiveNodeInstance rightNodeInstance = generate(rightNode, ctx);
			if (rightNodeInstance != null) {
				ruleInstance.getNodeInstances().add(rightNodeInstance);
			} else {
				log.debug("failed {}", rightNode);
				return null;
			}
		}
		return new RecursiveNodeInstance(rule.getLeft(), ruleInstance);
	}
	
}
