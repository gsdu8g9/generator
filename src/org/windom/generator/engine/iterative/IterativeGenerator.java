package org.windom.generator.engine.iterative;

import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.engine.RuleInstance;
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
		
		IterativeNodeInstance currentNode = new IterativeNodeInstance(node, ctx);
		
		IterativeNodeInstance successNode = new IterativeNodeInstance(new Symbol("#success"), null);
		IterativeNodeInstance failureNode = new IterativeNodeInstance(new Symbol("#failure"), null);
		LinkUtils.link(failureNode, currentNode);
		LinkUtils.link(currentNode, successNode);

		while (true) {
			log.info("Generating {}", currentNode);
			
			Rule rule = chooseAndRemoveRule(currentNode.getApplicableRules());
			
			RuleInstance<IterativeNodeInstance> ruleInstance = new RuleInstance<IterativeNodeInstance>(rule);
			for (Node rightNode : rule.getRight()) {
				IterativeNodeInstance rightNodeInstance = new IterativeNodeInstance(rightNode, null);
				ruleInstance.getNodeInstances().add(rightNodeInstance);
			}
			currentNode.setRuleInstance(ruleInstance);			
			LinkUtils.link(currentNode, ruleInstance.getNodeInstances());
			
			currentNode = LinkUtils.leftmostOnLimit(currentNode);
			
			log.info(">>>>{}", currentNode);
			
			while (!currentNode.needsGeneration()) {
				currentNode = LinkUtils.nextOnLimit(currentNode);
				log.info(">>{}", currentNode);
			}
			
			if (currentNode == successNode) break;			
			
		}

		return currentNode.getPrev();
		
	}
	
}
