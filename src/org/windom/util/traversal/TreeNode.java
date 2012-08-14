package org.windom.util.traversal;

import java.util.List;

public interface TreeNode<T extends TreeNode<T>> {

	public List<T> getChildren();
	
}
