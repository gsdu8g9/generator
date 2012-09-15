package org.windom.generator.engine.iterative;

import java.util.Arrays;
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
		
		Instance instance = new Instance();
		instance.generate(initial);
		
		RuleInstance ruleInstance = initial.getNodeInstance().getRuleInstance();
		NodeInstance nodeInstance = (ruleInstance != null) ? ruleInstance.getNodeInstances().get(0) : null;

		if (log.isDebugEnabled()) {
			log.debug("cycles: {}", instance.cycles);
			log.debug("success: {}", nodeInstance != null);
			log.debug("final context: {}", instance.currentCtx);
		}
			
		return nodeInstance;
	}
	
	private class Instance {
		
		private int cycles;
		private Waypoint current;
		private Waypoint backtrack;
		private int expandIdx;
		private GeneratorContext currentCtx;
		
		private void generate(Waypoint start) {
			current = start;
			backtrack = start;
			expandIdx = 0;
			currentCtx = start.getBacktrackCtx().branch();
			
			mainCycle: for(cycles=0;; cycles++) {
				if (log.isDebugEnabled())
					logCycle();
				
				NodeInstance nodeInstance = current.getNodeInstance();
				Node node = nodeInstance.getNode();
				RuleInstance ruleInstance = nodeInstance.getRuleInstance();
				
				if (ruleInstance == null) {
					if (current.getApplicableRules().isEmpty()) {
						log.debug("no rules to apply");
						if (!backtrack()) break;
						continue;
					}
					Rule rule = chooseAndRemoveRule(current.getApplicableRules());
					ruleInstance = new RuleInstance(rule);
					nodeInstance = new NodeInstance(node, ruleInstance);
					current.setNodeInstance(nodeInstance);
					expandIdx = 0;
				}
				
				List<Node> ruleRight = ruleInstance.getRule().getRight();
				
				innerCycle: while (expandIdx < ruleRight.size()) {
					Node childNode = ruleRight.get(expandIdx);
					if (childNode instanceof Terminal) {
						log.debug("@{} terminal {}", expandIdx, childNode);
						setChild(nodeInstance, expandIdx++, new NodeInstance(childNode));
					} else if (childNode instanceof Annotated) {
						Annotated annotated = (Annotated) childNode;
						switch (annotated.getAnnotation()) {
						case PERM:
							NodeInstance permNodeInstance = currentCtx.getPermNodeInstance(annotated);
							boolean bound = permNodeInstance != null;
							log.debug("@{} {} bound: {}", expandIdx, annotated, bound);
							if (!bound) break innerCycle;
							setChild(nodeInstance, expandIdx++, permNodeInstance);
							break;
						case ADD_TAG:
							log.debug("@{} {} applied", expandIdx, annotated);
							currentCtx.addTag(annotated.getNode().getName());
							setChild(nodeInstance, expandIdx++, new NodeInstance(childNode));
							break;
						case DEL_TAG:
							log.debug("@{} {} applied", expandIdx, annotated);
							currentCtx.delTag(annotated.getNode().getName());
							setChild(nodeInstance, expandIdx++, new NodeInstance(childNode));
							break;
						case CHECK_TAG:
							boolean result = currentCtx.checkTag(annotated.getNode().getName());
							log.debug("@{} {} result: {}", expandIdx, annotated, result);
							if (!result) {
								if (!backtrack()) return;
								continue mainCycle;
							}
							setChild(nodeInstance, expandIdx++, new NodeInstance(childNode));
							break;
						default:
							break innerCycle;
						}
					} else {
						break innerCycle;
					}
				}
				
				if (expandIdx < ruleRight.size()) {
					Node childNode = ruleRight.get(expandIdx);
					log.debug("@{} cycle for {}", expandIdx, childNode);
					current = new Waypoint(backtrack, current, expandIdx, childNode, 
							(expandIdx > 0) ? currentCtx.branch() : current.getBacktrackCtx());
					if (current.isDecisionPoint()) {
						backtrack = current;
					}
				} else {
					if (node instanceof Annotated) {
						Annotated annotated = (Annotated) node;
						switch (annotated.getAnnotation()) {
						case PERM:
							log.debug("{} bound", annotated);
							currentCtx.setPermNodeInstance(annotated, nodeInstance);
							if (annotated.getNode() instanceof Symbol) {
								currentCtx.addTag(annotated.getNode().getName());
							}
							if (!goup()) return;
							break;
						case SUCCEEDS:
							log.debug("{} succeeded", node);
							currentCtx = current.getBacktrackCtx().branch();
							backtrack = current.getBacktrack();
							current.setNodeInstance(new NodeInstance(node));
							if (!goup()) return;
							break;
						case FAILS:
							log.debug("{} failed", node);
							backtrack = current.getBacktrack();
							if (!backtrack()) return;
							break;
						default:
							throw new RuntimeException("impossibru");
						}
					} else {
						log.debug("{} completed", node);
						currentCtx.addTag(node.getName());
						if (!goup()) return;
					}
				}
			}
		}
		
		private void logCycle() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\nwaypoint: "); sb.append(current);
			sb.append("\nctx: "); sb.append(currentCtx);
			sb.append("\nbacktrack:\n");
			for (Waypoint bt = backtrack; bt != null; bt = bt.getBacktrack()) {
				sb.append(bt);
				sb.append('\n');
			}
			log.debug("{}", sb);
		}
		
		private boolean backtrack() {
			log.debug("backtracking to {}", backtrack);
			current = backtrack;
			if (current == null) return false;
			boolean backtrackExhausted = !backtrack.isDecisionPoint();
			if (backtrackExhausted) {
				backtrack = backtrack.getBacktrack();
			}
			Node currentNode = current.getNodeInstance().getNode();
			current.setNodeInstance(new NodeInstance(currentNode));
			currentCtx = current.getBacktrackCtx().branch();
			if (backtrackExhausted && Annotated.has(currentNode, Annotation.FAILS)) {
				log.debug("{} succeeded", currentNode);
				return goup();
			} else {
				return true;
			}
		}
		
		private boolean goup() {
			log.debug("going up");			
			NodeInstance nodeInstance = current.getNodeInstance();			
			expandIdx = current.getParentIdx();
			current = current.getParent();
			if (current == null) return false;			
			setChild(current.getNodeInstance(), expandIdx++, nodeInstance);			
			return true;
		}
		
	}
	
	private static void setChild(NodeInstance parentInstance, int idx, NodeInstance childInstance) {
		List<NodeInstance> childInstances = parentInstance.getRuleInstance().getNodeInstances();
		if (idx == childInstances.size()) {
			childInstances.add(childInstance);				
		} else {
			childInstances.set(idx, childInstance);
		}
	}
	
}
