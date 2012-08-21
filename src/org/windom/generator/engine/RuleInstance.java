package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Rule;

public class RuleInstance<N extends NodeInstance<N>> {

	private final Rule rule;
	private final List<N> nodeInstances;
	
	public RuleInstance(Rule rule) {
		this.rule = rule;
		this.nodeInstances = new ArrayList<N>(rule.getRight().size());
	}
	
	public Rule getRule() {
		return rule;
	}
	public List<N> getNodeInstances() {
		return nodeInstances;
	}
	
}
