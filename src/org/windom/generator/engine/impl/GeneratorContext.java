package org.windom.generator.engine.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.windom.generator.definition.AnnotatedNonterminal;
import org.windom.generator.engine.NodeInstance;

public class GeneratorContext {
	
	private Map<String,NodeInstance> permNodes = new HashMap<String,NodeInstance>();
	private Set<String> tags = new HashSet<String>();

	public GeneratorContext branch() {
		GeneratorContext branch = new GeneratorContext();
		branch.permNodes.putAll(permNodes);
		branch.tags.addAll(tags);
		return branch;
	}
	
	public void merge(GeneratorContext branch) {
		permNodes.putAll(branch.permNodes);
		tags.addAll(branch.tags);
	}
	
	public NodeInstance getPermNodeInstance(AnnotatedNonterminal node) {
		return permNodes.get(node.getNonterminal().getName());
	}
	
	public void setPermNodeInstance(AnnotatedNonterminal node, NodeInstance nodeInstance) {
		permNodes.put(node.getNonterminal().getName(), nodeInstance);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void delTag(String tag) {
		tags.remove(tag);
	}
	
	public boolean checkTag(String tag) {
		return tags.contains(tag);
	}
	
}
