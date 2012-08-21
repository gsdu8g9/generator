package org.windom.generator.engine;

import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.util.traversal.Traversal;

public class TreeInstance<N extends NodeInstance<N>> {

	private final N startInstance;
	
	public TreeInstance(N startInstance) {
		this.startInstance = startInstance;
	}

	public List<Node> getLimit(boolean onlyTerminals) {
		Limiter<N> limiter = new Limiter<N>(onlyTerminals);
		Traversal.depthFirst(startInstance, limiter, limiter);
		return limiter.getLimit();
	}
	
	public N getStartInstance() {
		return startInstance;
	}
	
}
