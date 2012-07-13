package org.windom.generator.input.plain;

import java.util.List;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.input.InputException;

public interface Builder {

	public Node buildNode(Nonterminal left, List<List<Node>> rights);
	public Definition build() throws InputException;
	
}
