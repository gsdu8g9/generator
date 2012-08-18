package org.windom.generator.definition;

import java.util.HashMap;
import java.util.Map;

import org.windom.util.traversal.Evaluator;
import org.windom.util.traversal.Visitor;

public class SymbolMapper implements Visitor<Node>, Evaluator<Node> {

	private final Map<String,Symbol> symbolMap = new HashMap<String,Symbol>();
	
	@Override
	public boolean prune(Node node) {
		return node.symbol() == null || symbolMap.containsKey(node.symbol().getName());
	}
	
	@Override
	public void visit(Node node) {
		Symbol symbol = node.symbol();
		symbolMap.put(symbol.getName(), symbol);		
	}
	
	public Map<String,Symbol> getSymbolMap() {
		return symbolMap;
	}
	
}
