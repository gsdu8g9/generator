package org.windom.generator.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Definition {

	private final Nonterminal start;	
	private Map<String,Nonterminal> nodeMap = null;
	
	public Definition(Nonterminal start) {
		this.start = start;
	}

	public Nonterminal getStart() {
		return start;
	}
	
	private Map<String,Nonterminal> getNodeMap() {
		if (nodeMap == null) {
			nodeMap = new HashMap<String,Nonterminal>();
			Queue<Nonterminal> queuedNodes = new LinkedList<Nonterminal>();
			queuedNodes.add(start);
			do {
				Nonterminal node = queuedNodes.remove();
				nodeMap.put(node.getName(), node);
				for (Rule rule : node.getRules()) {
					for (Node rightNode : rule.getRight()) {
						if (rightNode instanceof Nonterminal &&
								!nodeMap.containsKey(((Nonterminal) rightNode).getName())) {
							queuedNodes.add((Nonterminal) rightNode);
						}
					}
				}
			} while (!queuedNodes.isEmpty());
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
