package org.windom.generator.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.windom.generator.definition.AnnotatedNonterminal;
import org.windom.generator.engine.NodeInstance;

public class GeneratorContext {

	private Map<String,NodeInstance> permNodes = new HashMap<String,NodeInstance>();
	
	public NodeInstance getPermNodeInstance(AnnotatedNonterminal node) {
		return permNodes.get(node.getNonterminal().getName());
	}
	
	public void setPermNodeInstance(AnnotatedNonterminal node, NodeInstance nodeInstance) {
		permNodes.put(node.getNonterminal().getName(), nodeInstance);
	}
	
}
