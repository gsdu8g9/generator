package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.util.traversal.TreeNode;

public class NodeInstance<N extends NodeInstance<N>> implements TreeNode<N> {

	protected final Node node;
	protected RuleInstance<N> ruleInstance;
	
	public NodeInstance(Node node, RuleInstance<N> ruleInstance) {
		this.node = node;
		this.ruleInstance = ruleInstance;
	}
	
	@Override
	public List<N> getChildren() {
		if (!isOnLimit()) {
			return ruleInstance.getNodeInstances();
		} else {
			return new ArrayList<N>(0);
		}
	}

	public boolean isOnLimit() {
		return ruleInstance == null;
	}
	
	public Node getNode() {
		return node;
	}
	public RuleInstance<N> getRuleInstance() {
		return ruleInstance;
	}
	public void setRuleInstance(RuleInstance<N> ruleInstance) {
		this.ruleInstance = ruleInstance;
	}
	
}
