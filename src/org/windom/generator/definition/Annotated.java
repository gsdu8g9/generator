package org.windom.generator.definition;

import java.util.List;

public class Annotated implements Node {

	private final Annotation annotation;
	private Node node;
	
	public Annotated(Annotation annotation, Node node) {
		this.annotation = annotation;
		this.node = node;
	}

	public static boolean has(Node node, Annotation annotation) {
		return node instanceof Annotated &&
			((Annotated) node).getAnnotation() == annotation;
	}
	
	@Override
	public String getName() {
		return getName(annotation, node); 
	}

	public static String getName(Annotation annotation, Node node) {
		return Character.toString(annotation.getMark()) + node;
	}

	@Override
	public Symbol getSymbol() {
		return node.getSymbol();
	}
	
	@Override
	public List<Node> getChildren() {
		return node.getChildren();
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Annotated other = (Annotated) obj;
		if (annotation != other.annotation)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	public Annotation getAnnotation() {
		return annotation;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	
}
