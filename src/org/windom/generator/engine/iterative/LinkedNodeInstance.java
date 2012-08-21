package org.windom.generator.engine.iterative;

import org.windom.generator.definition.Node;
import org.windom.generator.engine.NodeInstance;

public class LinkedNodeInstance<N extends NodeInstance<N>> extends NodeInstance<N> {

	private N parent;
	private N prev;
	private N next;
	
	public LinkedNodeInstance(Node node) {
		super(node, null);
	}

	public N getParent() {
		return parent;
	}
	public void setParent(N parent) {
		this.parent = parent;
	}
	public N getPrev() {
		return prev;
	}
	public void setPrev(N prev) {
		this.prev = prev;
	}
	public N getNext() {
		return next;
	}
	public void setNext(N next) {
		this.next = next;
	}
	
}
