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
	
	@Override
	protected NodeInstance generate(Node node, GeneratorContext ctx) {
		Waypoint initial = new Waypoint(null, null, 0, node);
		generate(initial);
		return initial.getNodeInstance();
	}
	
	protected void generate(Waypoint current) {	
		
		Waypoint backtrack = null;
		int which = 0;
		
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
				which = current.getParentIdx();
				current = current.getParent();
				if (current == null) break;				
				setPar(current, which, nodeInstance);
				which++;
				
				if (((Terminal) node).getText().equals("c")) {
					// backtrack
					log.info("backtracking to {}", backtrack);
					current = backtrack;
					if (current == null) break;
					backtrack = backtrack.getBacktrack();
					current.setNodeInstance(new NodeInstance(current.getNodeInstance().getNode()));
					continue;
				}
			} else if (node instanceof Symbol) {
				RuleInstance ruleInstance = nodeInstance.getRuleInstance();
				
				if (ruleInstance == null) {
					if (current.getApplicableRules().isEmpty()) {
						// backtrack
						log.info("backtracking to {}", backtrack);
						current = backtrack;
						if (current == null) break;
						backtrack = backtrack.getBacktrack();
						current.setNodeInstance(new NodeInstance(current.getNodeInstance().getNode()));
						continue;
					}
					Rule rule = chooseAndRemoveRule(current.getApplicableRules());
					ruleInstance = new RuleInstance(rule);
					nodeInstance = new NodeInstance(nodeInstance.getNode(), ruleInstance);
					current.setNodeInstance(nodeInstance);
					which = 0;
				}
				
				int todo = getTodo(ruleInstance, which);
				log.info("todo {}", todo);
				
				if (todo < 0) {
					which = current.getParentIdx();
					current = current.getParent();
					if (current == null) break;
					setPar(current, which, nodeInstance);
					which++;
				} else {
					Node nextNode = ruleInstance.getRule().getRight().get(todo);
					current = new Waypoint(backtrack, current, todo, nextNode);
					if (current.isDecisionPoint()) {
						backtrack = current;
					}
				}
			} else {
				throw new RuntimeException("not yet ;)");
			}
		}
	}
	
	private void setPar(Waypoint wp, int idx, NodeInstance nodeInstance) {
		List<NodeInstance> is = wp.getNodeInstance().getRuleInstance().getNodeInstances();
		if (idx > is.size()) {
			throw new RuntimeException("na ez baj");
		} else if (idx == is.size()) {
			is.add(nodeInstance);
		} else {
			is.set(idx, nodeInstance);
		}
	}
	
	private int getTodo(RuleInstance ruleInstance, int which) {
		if (which >= ruleInstance.getRule().getRight().size())
			which = -1;
		return which;
	}
	
}
