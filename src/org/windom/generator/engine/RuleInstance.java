package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Rule;

public class RuleInstance {

	private final Rule rule;
	private final List<NodeInstance> nodeInstances;
	
	public RuleInstance(Rule rule) {
		this.rule = rule;
		this.nodeInstances = new ArrayList<NodeInstance>(rule.getRight().size());
	}
	
	public Rule getRule() {
		return rule;
	}
	public List<NodeInstance> getNodeInstances() {
		return nodeInstances;
	}
	
}
