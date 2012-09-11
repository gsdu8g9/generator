package org.windom.generator.definition;

import org.windom.util.traversal.TreeNode;

public interface Node extends TreeNode<Node> {

	public String getName();
	public Symbol getSymbol();
	
}
