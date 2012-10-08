package org.windom.generator.definition;

import java.util.HashMap;
import java.util.Map;

import org.windom.generator.util.traversal.Evaluator;
import org.windom.generator.util.traversal.Visitor;

public class SymbolMapper implements Visitor<Node>, Evaluator<Node> {

	private final Map<String,Symbol> symbolMap = new HashMap<String,Symbol>();
	
	@Override
	public boolean prune(Node node) {
		return node.getSymbol() == null || symbolMap.containsKey(node.getSymbol().getName());
	}
	
	@Override
	public void visit(Node node) {
		Symbol symbol = node.getSymbol();
		symbolMap.put(symbol.getName(), symbol);
	}
	
	public Map<String,Symbol> getSymbolMap() {
		return symbolMap;
	}
	
}
