package org.windom.generator.definition;

import java.util.Collection;
import java.util.Map;

import org.windom.util.traversal.Traversal;

public class Definition {

	private final Node start;	
	private Map<String,Nonterminal> nodeMap = null;
	
	public Definition(Node start) {
		this.start = start;
	}

	public Node getStart() {
		return start;
	}
	
	private Map<String,Nonterminal> getNodeMap() {
		if (nodeMap == null) {
			Mapper nodeMapper = new Mapper();
			Traversal.breadthFirst(start, nodeMapper, nodeMapper);
			nodeMap = nodeMapper.getNodeMap();
		}
		return nodeMap;
	}
	
	public Collection<Nonterminal> nodes() {
		return getNodeMap().values();
	}
	
	public Nonterminal node(String name) {
		return getNodeMap().get(name);
	}
	
	public String dump() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Nonterminal node : nodes()) {
			for (Rule rule : node.getRules()) {
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
