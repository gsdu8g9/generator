package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
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
		this.applicableRules = new ArrayList<Rule>(node.symbol().getRules());
		this.backtrack = isDecisionPoint() ? backtrack : null;
		this.backtrackCtx = backtrackCtx;
	}

	public boolean isDecisionPoint() {
		return applicableRules.size() > 1;
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
