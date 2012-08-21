package org.windom.generator.engine.recursive;

import org.windom.generator.definition.Node;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.RuleInstance;

public class RecursiveNodeInstance extends NodeInstance<RecursiveNodeInstance> {

	public RecursiveNodeInstance(Node node, RuleInstance<RecursiveNodeInstance> rule) {
		super(node, rule);
	}
	
	public RecursiveNodeInstance(Node node) {
		super(node, null);
	}
	
}
