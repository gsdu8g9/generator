package org.windom.generator.definition;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	public static final int PROBABILITY_PRIORITY_VAL = 1000;
	
	private final int probability;
	private final Symbol left;
	private final List<Node> right;

	public Rule(int probability, Symbol left, List<Node> right) {
		this.probability = probability;
		this.left = left;
		this.right = right;
	}
	
	public Rule(Symbol left) {
		this(0, left, new ArrayList<Node>());
	}
	
	public Rule(int probability) {
		this(probability, null, new ArrayList<Node>());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(left);
		sb.append(" ->");
		if (probability != 0) {
			sb.append(" [");
			sb.append(probability);
			sb.append(']');
		}
		for (Node node : right) {
			sb.append(' ');
			sb.append(node);
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		Rule other = (Rule) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	public int getProbability() {
		return probability;
	}
	public Symbol getLeft() {
		return left;
	}
	public List<Node> getRight() {
		return right;
	}
	
}
