package org.windom.generator.definition;

import java.util.ArrayList;
import java.util.List;

public class Nonterminal implements Node {

	private final String name;
	private final List<Rule> rules;
	
	public Nonterminal(String name) {
		this.name = name;
		this.rules = new ArrayList<Rule>();
	}
	
	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<Node>();
		for (Rule rule : rules) {
			for (Node rightNode : rule.getRight()) {
				children.add(rightNode);
			}
		}
		return children;
	}

	@Override
	public Nonterminal nonterminal() {
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Nonterminal other = (Nonterminal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}
	public List<Rule> getRules() {
		return rules;
	}
	
}
