package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.engine.NodeInstance;

public class Waypoint {

	private final Waypoint backtrack;
	private final Waypoint parent;
	private final int parentIdx;
	private NodeInstance nodeInstance;
	private final List<Rule> applicableRules;
	
	public Waypoint(Waypoint backtrack, Waypoint parent, int parentIdx, Node node) {
		this.parent = parent;
		this.parentIdx = parentIdx;
		this.nodeInstance = new NodeInstance(node);
		this.applicableRules = (node != null && node.symbol() != null)
				? new ArrayList<Rule>(node.symbol().getRules())
				: null;
		this.backtrack = isDecisionPoint()
				? backtrack
				: null;
	}

	public boolean isDecisionPoint() {
		return applicableRules != null && applicableRules.size() > 1;
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
	
}
