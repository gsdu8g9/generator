package org.windom.generator.postprocessor.impl;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.generator.postprocessor.PostProcessor;

public class PostProcessorImpl<N extends NodeInstance<N>> implements PostProcessor<N> {

	@Override
	public String process(TreeInstance<N> treeInstance) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Node node : treeInstance.getLimit(true)) {
			if (first) {
				first = false;
			} else {
				sb.append(' ');
			}
			sb.append(((Terminal) node).getText());
		}
		return sb.toString();
	}
	
}
