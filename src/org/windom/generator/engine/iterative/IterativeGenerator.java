package org.windom.generator.engine.iterative;

import java.util.List;
import java.util.Random;

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
	 * 1) optimization: don't create waypoints for terminals
	 * 
	 */
	
	@Override
	protected NodeInstance generate(Node node, GeneratorContext ctx) {
		Waypoint initial = new Waypoint(null, null, 0, node);
		boolean success = new Instance().generate(initial);
		log.info(">> success: {}", success);
		return initial.getNodeInstance();
	}
	
	private class Instance {
		
		private Waypoint current;
		private Waypoint backtrack;
		private int expandIdx;
		
		private boolean generate(Waypoint start) {			
			current = start;
			backtrack = null;
			expandIdx = 0;
			
			while (true) {
				
				NodeInstance nodeInstance = current.getNodeInstance();
				Node node = nodeInstance.getNode();
				
				log.info("");
				log.info("");
				log.info("==============================");
				log.info("waypoint: {}", current);
				log.info("backtrack:");
				for (Waypoint bt = backtrack; bt != null; bt = bt.getBacktrack()) {
					log.info("{}", bt);
				}
				log.info("==============================");
				
				if (node instanceof Terminal) {
					log.info("terminal {}", node);
					if (!goup()) return true;
					
					if (((Terminal) node).getText().equals("c")) {
						if (!backtrack()) return false;
						continue;
					}
				} else if (node instanceof Symbol) {
					RuleInstance ruleInstance = nodeInstance.getRuleInstance();
					
					if (ruleInstance == null) {
						if (current.getApplicableRules().isEmpty()) {
							if (!backtrack()) return false;
							continue;
						}
						Rule rule = chooseAndRemoveRule(current.getApplicableRules());
						ruleInstance = new RuleInstance(rule);
						nodeInstance = new NodeInstance(nodeInstance.getNode(), ruleInstance);
						current.setNodeInstance(nodeInstance);
						expandIdx = 0;
					}
					
					if (expandIdx >= ruleInstance.getRule().getRight().size()) {
						if (!goup()) return true;
					} else {
						log.info("doing {}", expandIdx);
						Node nextNode = ruleInstance.getRule().getRight().get(expandIdx);
						current = new Waypoint(backtrack, current, expandIdx, nextNode);
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
			backtrack = backtrack.getBacktrack();
			current.setNodeInstance(new NodeInstance(current.getNodeInstance().getNode()));
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
