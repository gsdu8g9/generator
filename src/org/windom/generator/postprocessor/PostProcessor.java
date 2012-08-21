package org.windom.generator.postprocessor;

import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;

public interface PostProcessor {

	public String process(TreeInstance<? extends NodeInstance<?>> treeInstance);
	
}
