package org.windom.generator.engine;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Terminal;
import org.windom.util.traversal.Evaluator;
import org.windom.util.traversal.Visitor;

public class Limiter<N extends NodeInstance<N>> implements Visitor<N>, Evaluator<N> {

	private final List<Node> limit = new ArrayList<Node>();
	private final boolean onlyTerminals;
	
	public Limiter(boolean onlyTerminals) {
		this.onlyTerminals = onlyTerminals;
	}
	
	@Override
	public boolean prune(N nodeInstance) {
		return nodeInstance.isOnLimit() && 
				onlyTerminals && 
				!(nodeInstance.getNode() instanceof Terminal);
	}
	
	@Override
	public void visit(N nodeInstance) {
		if (nodeInstance.isOnLimit()) {
			limit.add(nodeInstance.getNode());
		}
	}
	
	public List<Node> getLimit() {
		return limit;
	}
	
}
