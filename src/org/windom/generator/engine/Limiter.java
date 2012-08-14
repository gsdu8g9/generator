package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.util.traversal.Evaluator;
import org.windom.util.traversal.Visitor;

public class Limiter implements Visitor<NodeInstance>, Evaluator<NodeInstance> {

	private final List<Node> limit = new ArrayList<Node>();
	
	@Override
	public boolean prune(NodeInstance nodeInstance) {
		return false;
	}
	
	@Override
	public void visit(NodeInstance nodeInstance) {
		if (nodeInstance.isOnLimit()) {
			limit.add(nodeInstance.getNode());
		}
	}
	
	public List<Node> getLimit() {
		return limit;
	}
	
}
