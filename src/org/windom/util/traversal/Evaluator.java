package org.windom.util.traversal;

public interface Evaluator<T extends TreeNode<T>> {

	public boolean prune(T treeNode);
	
}
