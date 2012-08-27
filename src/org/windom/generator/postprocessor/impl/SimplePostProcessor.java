package org.windom.generator.postprocessor.impl;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Terminal;
import org.windom.generator.engine.TreeInstance;
import org.windom.generator.postprocessor.PostProcessor;

public class SimplePostProcessor implements PostProcessor {

	@Override
	public String process(TreeInstance treeInstance) {
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
