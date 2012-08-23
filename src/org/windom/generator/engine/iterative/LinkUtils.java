package org.windom.generator.engine.iterative;

import java.util.List;

public class LinkUtils {

	public static <N extends LinkedNodeInstance<N>> void link(N nodeInstance1, N nodeInstance2) {
		nodeInstance1.setNext(nodeInstance2);
		nodeInstance2.setPrev(nodeInstance1);
	}
	
	public static <N extends LinkedNodeInstance<N>> void link(N parentInstance, List<N> childInstances) {
		N prevInstance = null;
		for (N childInstance : childInstances) {
			childInstance.setParent(parentInstance);
			if (prevInstance != null) {
				link(prevInstance, childInstance);
			}
			prevInstance = childInstance;
		}
	}
	
	public static <N extends LinkedNodeInstance<N>> N leftmostOnLimit(N nodeInstance) {
		while (!nodeInstance.isOnLimit()) {
			nodeInstance = nodeInstance.getChildren().get(0);
		}
		return nodeInstance;
	}
	
	public static <N extends LinkedNodeInstance<N>> N rightmostOnLimit(N nodeInstance) {
		while (!nodeInstance.isOnLimit()) {
			List<N> children = nodeInstance.getChildren();
			nodeInstance = children.get(children.size() - 1);
		}
		return nodeInstance;
	}
	
	public static <N extends LinkedNodeInstance<N>> N prevOnLimit(N nodeInstance) {
		while (nodeInstance.getPrev() == null) {
			nodeInstance = nodeInstance.getParent();
		}
		return rightmostOnLimit(nodeInstance.getPrev());
	}
	
	public static <N extends LinkedNodeInstance<N>> N nextOnLimit(N nodeInstance) {
		while (nodeInstance.getNext() == null) {
			nodeInstance = nodeInstance.getParent();
		}
		return leftmostOnLimit(nodeInstance.getNext());
	}
	
}
