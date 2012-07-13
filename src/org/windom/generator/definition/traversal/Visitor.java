package org.windom.generator.definition.traversal;

import org.windom.generator.definition.Nonterminal;

public interface Visitor {

	public void visit(Nonterminal node);
	
}
