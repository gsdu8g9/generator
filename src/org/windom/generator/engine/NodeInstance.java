package org.windom.generator.engine;

import java.util.Collections;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.util.traversal.TreeNode;

public class NodeInstance implements TreeNode<NodeInstance> {

	private final Node node;
	private final RuleInstance ruleInstance;
	
	public NodeInstance(Node node, RuleInstance ruleInstance) {
		this.node = node;
		this.ruleInstance = ruleInstance;
	}
	
	public NodeInstance(Node node) {
		this(node, null);
	}
	
	@Override
	public List<NodeInstance> getChildren() {
		if (!isOnLimit()) {
			return ruleInstance.getNodeInstances();
		} else {
			return Collections.emptyList();
		}
	}

	public boolean isOnLimit() {
		return ruleInstance == null;
	}
	
	public Node getNode() {
		return node;
	}
	public RuleInstance getRuleInstance() {
		return ruleInstance;
	}
	
}
