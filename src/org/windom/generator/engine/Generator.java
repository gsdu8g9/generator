package org.windom.generator.engine;

public interface Generator<N extends NodeInstance<N>> {
	
	public TreeInstance<N> generate() throws GeneratorException;
	
}
