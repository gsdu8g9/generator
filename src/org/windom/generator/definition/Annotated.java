package org.windom.generator.definition;

import java.util.List;

public class Annotated extends Nonterminal {

	private final Annotation annotation;
	private Nonterminal nonterminal;
	
	public Annotated(Annotation annotation, Nonterminal nonterminal) {
		this.annotation = annotation;
		this.nonterminal = nonterminal;
	}

	public String getName() {
		return getName(annotation, nonterminal); 
	}

	public static String getName(Annotation annotation, Nonterminal nonterminal) {
		return Character.toString(annotation.getMark()) + nonterminal;
	}
	
	@Override
	public List<Node> getChildren() {
		return nonterminal.getChildren();
	}

	@Override
	public Symbol symbol() {
		return nonterminal.symbol();
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
		result = prime * result
				+ ((nonterminal == null) ? 0 : nonterminal.hashCode());
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
		if (nonterminal == null) {
			if (other.nonterminal != null)
				return false;
		} else if (!nonterminal.equals(other.nonterminal))
			return false;
		return true;
	}

	public Annotation getAnnotation() {
		return annotation;
	}
	public Nonterminal getNonterminal() {
		return nonterminal;
	}
	public void setNonterminal(Nonterminal nonterminal) {
		this.nonterminal = nonterminal;
	}
	
}
