package org.windom.generator.input.plain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Builder;

public class BuilderImpl implements Builder {
	
	private static final Logger log = LoggerFactory.getLogger(BuilderImpl.class);
	
	private final Map <String,Nonterminal> nodeMap = new HashMap<String,Nonterminal>();
	private Nonterminal start = null;
	
	private static final String PHANTOM_NODE_MASK = "$%d";
	private int phantomNodeCount = 0;
	
	@Override
	public Definition build() throws InputException {
		Definition definition = new Definition(start);
		checkDefinition(definition);
		return definition;
	}
	
	@Override
	public Node buildNode(Nonterminal left, List<List<Node>> rights) {
		Nonterminal node = resolveNonterminal(left);
		if (rights.size() == 0) {
			rights.add(new ArrayList<Node>());
		}
		for (List<Node> right : rights) {
			for (int i=0; i<right.size(); i++) {
				right.set(i,resolveNode(right.get(i)));
			}
			Rule rule = new Rule(node, right);
			if (!node.getRules().contains(rule)) {
				node.getRules().add(rule);
			} else {
				log.warn("Ignoring duplicate rule: {}",rule);
			}
		}
		if (start == null && left != null) {
			start = node;
		}
		return node;
	}
	
	private Node resolveNode(Node node) {
		if (node instanceof Nonterminal) {
			return resolveNonterminal((Nonterminal) node);
		} else {
			return node;
		}
	}
	
	private Nonterminal resolveNonterminal(Nonterminal node) {
		if (node == null) {
			node = makePhantomNode();	
		} else if (nodeMap.containsKey(node.getName())) {
			node = nodeMap.get(node.getName());
		}
		nodeMap.put(node.getName(),node);
		return node;
	}
	
	private Nonterminal makePhantomNode() {
		return new Nonterminal(String.format(PHANTOM_NODE_MASK,
				phantomNodeCount++));
	}

	private void checkDefinition(Definition definition) throws InputException {
		Collection<Nonterminal> accessibleNodes = definition.nodes();
		//
		// Check accessibility
		//
		for (Nonterminal node : nodeMap.values()) {
			if (!accessibleNodes.contains(node)) {
				log.warn("Unreachable node: {}",node);
			}
		}
		//
		// Check completeness
		//
		for (Nonterminal node : accessibleNodes) {
			if (node.getRules().size() == 0) {
				throw new InputException("Incomplete node: " + node);
			}
		}
		//
		// TODO check termination
		//
	}
	
}
