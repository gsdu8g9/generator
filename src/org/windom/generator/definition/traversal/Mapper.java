package org.windom.generator.definition.traversal;

import java.util.HashMap;
import java.util.Map;

import org.windom.generator.definition.Nonterminal;

public class Mapper implements Visitor, Evaluator {

	private final Map<String,Nonterminal> nodeMap = new HashMap<String,Nonterminal>();
	
	@Override
	public boolean prune(Nonterminal node) {
		return nodeMap.containsKey(node.getName());
	}
	
	@Override
	public void visit(Nonterminal node) {
		nodeMap.put(node.getName(), node);
	}
	
	public Map<String, Nonterminal> getNodeMap() {
		return nodeMap;
	}
	
}
