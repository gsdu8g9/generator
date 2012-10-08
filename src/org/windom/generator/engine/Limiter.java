package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Terminal;
import org.windom.generator.util.traversal.Visitor;

public class Limiter implements Visitor<NodeInstance> {

	private final List<Node> limit = new ArrayList<Node>();
	private final boolean onlyTerminals;
	
	public Limiter(boolean onlyTerminals) {
		this.onlyTerminals = onlyTerminals;
	}
	
	@Override
	public boolean prune(NodeInstance nodeInstance) {
		return nodeInstance.isOnLimit() && 
				onlyTerminals && 
				!(nodeInstance.getNode() instanceof Terminal);
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
