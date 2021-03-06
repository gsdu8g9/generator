package org.windom.generator.definition;

import java.util.ArrayList;
import java.util.List;

public class Symbol implements Node {

	private final String name;
	private final List<Rule> rules;
	
	public Symbol(String name) {
		this.name = name;
		this.rules = new ArrayList<Rule>();
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Symbol getSymbol() {
		return this;
	}
	
	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<Node>();
		for (Rule rule : rules) {
			children.addAll(rule.getRight());
		}
		return children;
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
		Symbol other = (Symbol) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public List<Rule> getRules() {
		return rules;
	}
	
}
