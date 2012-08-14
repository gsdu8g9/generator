package org.windom.generator.engine;

import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.util.traversal.Traversal;

public class TreeInstance {

	private final NodeInstance startInstance;
	
	public TreeInstance(NodeInstance startInstance) {
		this.startInstance = startInstance;
	}

	public List<Node> getLimit() {
		Limiter limiter = new Limiter();
		Traversal.depthFirst(startInstance, limiter, limiter);
		return limiter.getLimit();
	}
	
	public NodeInstance getStartInstance() {
		return startInstance;
	}
	
}
