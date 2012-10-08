package org.windom.generator.definition;

import java.util.Collection;
import java.util.Map;

import org.windom.generator.util.traversal.Traversal;

public class Definition {

	private final Node start;
	private Map<String,Symbol> symbolMap = null;
	
	public Definition(Node start) {
		this.start = start;
	}

	public Node getStart() {
		return start;
	}
	
	private Map<String,Symbol> getSymbolMap() {
		if (symbolMap == null) {
			SymbolMapper symbolMapper = new SymbolMapper();
			Traversal.breadthFirst(start, symbolMapper, symbolMapper);
			symbolMap = symbolMapper.getSymbolMap();
		}
		return symbolMap;
	}
	
	public Collection<Symbol> symbols() {
		return getSymbolMap().values();
	}
	
	public Symbol node(String name) {
		return getSymbolMap().get(name);
	}
	
	public String dump() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Symbol symbol : symbols()) {
			for (Rule rule : symbol.getRules()) {
				if (first) {
					first = false;
				} else {
					sb.append('\n');
				}
				sb.append(rule);
			}
		}
		return sb.toString();
	}
	
}
