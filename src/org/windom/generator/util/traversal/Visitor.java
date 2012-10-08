package org.windom.generator.util.traversal;

public interface Visitor<T extends TreeNode<T>> {

	public boolean prune(T treeNode);
	public void visit(T treeNode);
	
}
