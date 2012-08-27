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
		
		IterativeNodeInstance currentInstance = new IterativeNodeInstance(node);
		currentInstance.setCtx(ctx);
		
		IterativeNodeInstance successInstance = new IterativeNodeInstance(new Symbol("#success"));
		IterativeNodeInstance failureInstance = new IterativeNodeInstance(new Symbol("#failure"));
		LinkUtils.link(failureInstance, currentInstance);
		LinkUtils.link(currentInstance, successInstance);

		while (true) {
			log.info("generating {}", currentInstance.getNode());
			
			generateSymbol(currentInstance);
			
			currentInstance = goDown(currentInstance);			
			
			while (!currentInstance.needsGeneration()) {
				while (currentInstance.getNext() == null) {
					currentInstance = goUp(currentInstance);
				}
				
				do {
					currentInstance = goRight(currentInstance);
				} while (!currentInstance.needsGeneration() && currentInstance.getNext() != null);
			}
			
			if (currentInstance == successInstance) break;			
		}

		return currentInstance.getPrev();	
	}
	
	private void generateSymbol(IterativeNodeInstance nodeInstance) {		
		Rule rule = chooseAndRemoveRule(nodeInstance.getApplicableRules());		
		RuleInstance<IterativeNodeInstance> ruleInstance = new RuleInstance<IterativeNodeInstance>(rule);
		for (Node rightNode : rule.getRight()) {
			IterativeNodeInstance childInstance = new IterativeNodeInstance(rightNode);
			ruleInstance.getNodeInstances().add(childInstance);
		}
		nodeInstance.setRuleInstance(ruleInstance);
		LinkUtils.link(nodeInstance, ruleInstance.getNodeInstances());
	}
	
	private IterativeNodeInstance goDown(IterativeNodeInstance nodeInstance) {		
		IterativeNodeInstance childInstance = nodeInstance.getChildren().get(0);
		log.info("go down {} ==> {}", nodeInstance.getNode(), childInstance.getNode());
		childInstance.setCtx(nodeInstance.getCtx().branch());
		return childInstance;
	}
	
	private IterativeNodeInstance goUp(IterativeNodeInstance nodeInstance) {
		IterativeNodeInstance parentInstance = nodeInstance.getParent();
		log.info("go up {} ==> {}", nodeInstance.getNode(), parentInstance.getNode());
		parentInstance.setCtx(nodeInstance.getCtx().branch());
		parentInstance.getCtx().addTag(parentInstance.getNode().symbol().getName());
		return parentInstance;
	}
	
	private IterativeNodeInstance goRight(IterativeNodeInstance nodeInstance) {
		IterativeNodeInstance rightInstance = nodeInstance.getNext();
		log.info("go right {} ==> {}", nodeInstance.getNode(), rightInstance.getNode());
		rightInstance.setCtx(nodeInstance.getCtx().branch());
		return rightInstance;
	}
	
}
