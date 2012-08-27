package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.engine.NodeInstance;

public class Waypoint {

	private final Waypoint prev;
	private final Waypoint parent;
	private final int parentIdx;
	private NodeInstance nodeInstance;
	private final List<Rule> applicableRules;
	
	public Waypoint(Waypoint prev, Waypoint parent, int parentIdx, Node node) {
		this.prev = prev;
		this.parent = parent;
		this.parentIdx = parentIdx;
		this.nodeInstance = new NodeInstance(node);
		this.applicableRules = (node != null && node.symbol() != null)
				? new ArrayList<Rule>(node.symbol().getRules())
				: null;
	}

	public Waypoint getPrev() {
		return prev;
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
