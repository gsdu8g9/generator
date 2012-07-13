package org.windom.generator.input;

import org.windom.generator.definition.Definition;

public interface Input {

	public Definition read() throws InputException;
	
}
