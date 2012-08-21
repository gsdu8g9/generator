package org.windom.generator.engine.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.windom.generator.definition.Annotated;
import org.windom.generator.engine.NodeInstance;

public class GeneratorContext<N extends NodeInstance<N>> {
	
	private Map<String,N> permNodes = new HashMap<String,N>();
	private Set<String> tags = new HashSet<String>();
	private GeneratorStats stats = new GeneratorStats();
	
	public GeneratorContext<N> branch() {
		GeneratorContext<N> branch = new GeneratorContext<N>();
		branch.permNodes.putAll(permNodes);
		branch.tags.addAll(tags);
		return branch;
	}
	
	public void merge(GeneratorContext<N> branch, boolean success) {
		mergeStats(branch, success);
		if (success) {
			permNodes.putAll(branch.permNodes);
			tags.addAll(branch.tags);
		}
	}
	
	public void mergeStats(GeneratorContext<N> branch, boolean success) {
		if (success) {
			stats.succeeded(branch.stats);
		} else {
			stats.failed(branch.stats);
		}
	}
	
	public N getPermNodeInstance(Annotated node) {
		return permNodes.get(node.symbol().getName());
	}
	
	public void setPermNodeInstance(Annotated node, N nodeInstance) {
		permNodes.put(node.symbol().getName(), nodeInstance);
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

	public GeneratorStats getStats() {
		return stats;
	}
	
}
