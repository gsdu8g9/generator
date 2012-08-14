package org.windom.util.traversal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Traversal {

	public static <T extends TreeNode<T>> void breadthFirst(T node, Visitor<T> visitor, Evaluator<T> evaluator) {
		Queue<T> queuedNodes = new LinkedList<T>();
		queuedNodes.add(node);
		do {
			node = queuedNodes.remove();
			if (!evaluator.prune(node)) {
				visitor.visit(node);
				queuedNodes.addAll(node.getChildren());
			}
		} while (!queuedNodes.isEmpty());
	}

	public static <T extends TreeNode<T>> void depthFirst(T node, Visitor<T> visitor, Evaluator<T> evaluator) {
		Stack<T> stackedNodes = new Stack<T>();
		stackedNodes.push(node);
		do {
			node = stackedNodes.pop();
			if (!evaluator.prune(node)) {				
				List<T> children = node.getChildren();
				for (int idx=children.size()-1; idx>=0; idx--) {
					stackedNodes.push(children.get(idx));
				}
				visitor.visit(node);
			}
		} while (!stackedNodes.isEmpty());
	}
	
}