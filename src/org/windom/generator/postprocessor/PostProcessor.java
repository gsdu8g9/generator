package org.windom.generator.postprocessor;

import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;

public interface PostProcessor<N extends NodeInstance<N>> {

	public String process(TreeInstance<N> treeInstance);
	
}
