package org.windom.util.traversal;

public interface Visitor<T extends TreeNode<T>> {

	public void visit(T treeNode);
	
}
