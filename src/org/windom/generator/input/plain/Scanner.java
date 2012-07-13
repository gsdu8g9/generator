package org.windom.generator.input.plain;

import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.symbol.Token;

public interface Scanner {

	public Token token() throws InputException;
	
}
