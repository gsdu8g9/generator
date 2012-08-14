package org.windom.generator.definition;

import java.util.HashMap;
import java.util.Map;

import org.windom.util.traversal.Evaluator;
import org.windom.util.traversal.Visitor;

public class Mapper implements Visitor<Node>, Evaluator<Node> {

	private final Map<String,Nonterminal> nodeMap = new HashMap<String,Nonterminal>();
	
	@Override
	public boolean prune(Node node) {
		return node.nonterminal() == null ||
				nodeMap.containsKey(node.nonterminal().getName());
	}
	
	@Override
	public void visit(Node node) {
		Nonterminal nonterminal = node.nonterminal();
		nodeMap.put(nonterminal.getName(), nonterminal);		
	}
	
	public Map<String, Nonterminal> getNodeMap() {
		return nodeMap;
	}
	
}
