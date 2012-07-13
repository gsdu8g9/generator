package org.windom.generator.definition.traversal;

import org.windom.generator.definition.Nonterminal;

public interface Evaluator {

	public boolean prune(Nonterminal node);
	
}
