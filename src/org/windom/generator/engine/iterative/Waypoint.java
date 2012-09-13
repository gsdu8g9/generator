package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.common.GeneratorContext;

public class Waypoint {

	private final Waypoint backtrack;
	private final Waypoint parent;
	private final int parentIdx;
	private NodeInstance nodeInstance;
	private final List<Rule> applicableRules;
	private final GeneratorContext backtrackCtx;
	
	public Waypoint(Waypoint backtrack, Waypoint parent, int parentIdx, Node node, GeneratorContext backtrackCtx) {
		this.parent = parent;
		this.parentIdx = parentIdx;
		this.nodeInstance = new NodeInstance(node);
		this.applicableRules = getApplicableRules(node);
		this.backtrack = isDecisionPoint() ? backtrack : null;
		this.backtrackCtx = backtrackCtx;
	}

	private static List<Rule> getApplicableRules(Node node) {
		if (node instanceof Annotated) {
			Annotated annotated = (Annotated) node;
			if (!(annotated.getNode() instanceof Symbol)) {
				Rule rule = new Rule(0, null, Collections.singletonList(annotated.getNode()));
				List<Rule> rules = new ArrayList<Rule>(1);
				rules.add(rule);
				return rules;
			}
		}
		return new ArrayList<Rule>(node.getSymbol().getRules());
	}
	
	public boolean isDecisionPoint() {
		return (applicableRules.size() > 1) || 
				(applicableRules.size() > 0 && (
						Annotated.has(nodeInstance.getNode(), Annotation.SUCCEEDS) ||
						Annotated.has(nodeInstance.getNode(), Annotation.FAILS)
					));
	}
	
	public String toString(boolean showLinks) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		sb.append(String.format("node: %s @%d; ", nodeInstance.getNode(), parentIdx));
		sb.append(String.format("rule: %s; ", nodeInstance.getRuleInstance() != null
				? nodeInstance.getRuleInstance().getRule()
				: "null"));
		sb.append(String.format("applicable: %s; ", applicableRules));
		if (showLinks) {
			sb.append(String.format("parent: %s; ", parent != null
				? parent.toString(false)
				: "null"));
			sb.append(String.format("backtrack: %s; ", backtrack != null
					? backtrack.toString(false)
					: "null"));
		}
		sb.append(String.format("backtrackCtx: %s; ", backtrackCtx));
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public Waypoint getBacktrack() {
		return backtrack;
	}
	public Waypoint getParent() {
		return parent;
	}
	public int getParentIdx() {
		return parentIdx;
	}
	public NodeInstance getNodeInstance() {
		return nodeInstance;
	}
	public void setNodeInstance(NodeInstance nodeInstance) {
		this.nodeInstance = nodeInstance;
	}
	public List<Rule> getApplicableRules() {
		return applicableRules;
	}
	public GeneratorContext getBacktrackCtx() {
		return backtrackCtx;
	}
	
}
