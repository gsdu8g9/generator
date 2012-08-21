package org.windom.generator.input.plain;

import java.util.List;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.input.InputException;

public interface Builder {

	public Symbol buildSymbol(Symbol left, List<Rule> rightSides) throws InputException;
	public Definition build() throws InputException;
	
}
