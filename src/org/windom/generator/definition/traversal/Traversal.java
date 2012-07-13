package org.windom.generator.definition.traversal;

import java.util.LinkedList;
import java.util.Queue;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;

public class Traversal {

	public static void breadthFirst(Nonterminal node, Visitor visitor, Evaluator evaluator) {
		Queue<Nonterminal> queuedNodes = new LinkedList<Nonterminal>();
		queuedNodes.add(node);
		do {
			Nonterminal queuedNode = queuedNodes.remove();
			if (!evaluator.prune(queuedNode)) {
				visitor.visit(queuedNode);
				for (Rule rule : queuedNode.getRules()) {
					for (Node rightNode : rule.getRight()) {
						if (rightNode instanceof Nonterminal) {
							queuedNodes.add((Nonterminal) rightNode);
						}
					}
				}
			}
		} while (!queuedNodes.isEmpty());
	}
	
	/*
	 * TODO Find a nonrecursive solution, like here:
	 * http://blogs.msdn.com/b/daveremy/archive/2010/03/16/non-recursive-post-order-depth-first-traversal.aspx 
	 */
	public static void postOrderDepthFirst(Nonterminal node, Visitor visitor, Evaluator evaluator) {
		if (!evaluator.prune(node)) {
			for (Rule rule : node.getRules()) {
				for (Node rightNode : rule.getRight()) {
					if (rightNode instanceof Nonterminal) {
						postOrderDepthFirst((Nonterminal)rightNode, visitor, evaluator);						
					}
				}
			}
			visitor.visit(node);
		}
	}
	
}
