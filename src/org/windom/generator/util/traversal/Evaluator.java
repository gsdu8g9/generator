package org.windom.generator.util.traversal;

public interface Evaluator<T extends TreeNode<T>> {

	public boolean prune(T treeNode);
	
}
