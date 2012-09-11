package org.windom.generator.engine.iterative;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;
import org.windom.generator.engine.common.AbstractGenerator;
import org.windom.generator.engine.common.GeneratorContext;

public class IterativeGenerator extends AbstractGenerator {

	public IterativeGenerator(Definition definition, Random rng) {
		super(definition, rng);
	}
	
	public IterativeGenerator(Definition definition) {
		super(definition, new Random());
	}
	
	/*
	 * TODO 
	 * 1) X optimization: don't create waypoints for terminals, bound perms, add/del/check-tags
	 * 2) infinite generation detection: if waypoint+ctx matches an older waypoint+ctx combination then
	 *    we are in an infinite cycle (?)
	 * 
	 */
	
	@Override
	protected NodeInstance generate(Node node, GeneratorContext ctx) {
		Symbol origin = new Symbol("#origin");
		origin.getRules().add(new Rule(0, origin, Arrays.asList(node)));
		Waypoint initial = new Waypoint(null, null, 0, origin, ctx);
		new Instance().generate(initial);
		RuleInstance ruleInstance = initial.getNodeInstance().getRuleInstance();
		log.info(">> success: {}", ruleInstance != null);
		return ruleInstance != null ? ruleInstance.getNodeInstances().get(0) : null;
	}
	
	private class Instance {
		
		private Waypoint current;
		private Waypoint backtrack;
		private int expandIdx;
		private GeneratorContext currentCtx;
		
		private void generate(Waypoint start) {			
			current = start;
			backtrack = start;
			expandIdx = 0;
			currentCtx = start.getBacktrackCtx();
			
			while (true) {
					
				NodeInstance nodeInstance = current.getNodeInstance();
				Node node = nodeInstance.getNode();
				
				log.info("");
				log.info("");
				log.info("==============================");
				log.info("waypoint: {}", current);
				log.info("ctx: {}", currentCtx);
				log.info("backtrack:");
				for (Waypoint bt = backtrack; bt != null; bt = bt.getBacktrack()) {
					log.info("{}", bt);
				}
				log.info("==============================");
				
				if (node instanceof Symbol) {
					RuleInstance ruleInstance = nodeInstance.getRuleInstance();
					
					if (ruleInstance == null) {
						if (current.getApplicableRules().isEmpty()) {
							if (!backtrack()) return;
							continue;
						}
						Rule rule = chooseAndRemoveRule(current.getApplicableRules());
						ruleInstance = new RuleInstance(rule);
						nodeInstance = new NodeInstance(nodeInstance.getNode(), ruleInstance);
						current.setNodeInstance(nodeInstance);
						expandIdx = 0;
					}
					
					if (expandIdx >= ruleInstance.getRule().getRight().size()) {
						currentCtx.addTag(((Symbol) node).getName());
						if (!goup()) return;
					} else {
						log.info("doing {}", expandIdx);
						Node nextNode = ruleInstance.getRule().getRight().get(expandIdx);
						if (nextNode instanceof Terminal) {
							log.info("terminal {}", nextNode);
							ruleInstance.getNodeInstances().add(new NodeInstance(nextNode));
							expandIdx++;
							continue;
						}
						if (nextNode instanceof Annotated) {
							Annotated annotated = (Annotated) nextNode;
							switch (annotated.getAnnotation()) {
							case ADD_TAG:
								log.info("{} applied", annotated);
								currentCtx.addTag(annotated.getNode().getName());
								ruleInstance.getNodeInstances().add(new NodeInstance(nextNode));
								expandIdx++;
								continue;
							case DEL_TAG:
								log.info("{} applied", annotated);
								currentCtx.delTag(annotated.getNode().getName());
								ruleInstance.getNodeInstances().add(new NodeInstance(nextNode));
								expandIdx++;
								continue;
							case CHECK_TAG:
								boolean result = currentCtx.checkTag(annotated.getNode().getName());
								log.info("{} result: {}", annotated, result);
								if (result) {
									ruleInstance.getNodeInstances().add(new NodeInstance(nextNode));
									expandIdx++;
									continue;
								} else {
									if (!backtrack()) return;
									continue;
								}
							}
								
						}
						current = new Waypoint(backtrack, current, expandIdx, nextNode, 
								(expandIdx > 0) ? currentCtx.branch() : current.getBacktrackCtx());
						if (current.isDecisionPoint()) {
							backtrack = current;
						}
					}
				} else {
					throw new RuntimeException("not yet ;)");
				}
			}
			
		}
		
		private boolean backtrack() {
			log.info("backtracking to {}", backtrack);
			current = backtrack;
			if (current == null) return false;
			if (!backtrack.isDecisionPoint()) {
				backtrack = backtrack.getBacktrack();
			}
			current.setNodeInstance(new NodeInstance(current.getNodeInstance().getNode()));
			currentCtx = current.getBacktrackCtx().branch();
			return true;
		}
		
		private boolean goup() {
			log.info("going up");
			
			NodeInstance nodeInstance = current.getNodeInstance();
			
			expandIdx = current.getParentIdx();
			current = current.getParent();
			if (current == null) return false;
			
			List<NodeInstance> nodeInstances = current
					.getNodeInstance()
					.getRuleInstance()
					.getNodeInstances();
			
			if (expandIdx == nodeInstances.size()) {
				nodeInstances.add(nodeInstance);
			} else {
				nodeInstances.set(expandIdx, nodeInstance);
			}
			
			expandIdx++;
			
			return true;
		}
		
	}
	
}
