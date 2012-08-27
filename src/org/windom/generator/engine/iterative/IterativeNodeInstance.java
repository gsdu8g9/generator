package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.engine.common.GeneratorContext;

public class IterativeNodeInstance extends LinkedNodeInstance<IterativeNodeInstance> {

	private GeneratorContext<IterativeNodeInstance> ctx;
	private final List<Rule> applicableRules;
	
	public IterativeNodeInstance(Node node) {
		super(node);
		this.ctx = null;
		this.applicableRules = (node != null && node.symbol() != null)
				? new ArrayList<Rule>(node.symbol().getRules())
				: null;
	}
	
	public boolean needsGeneration() {
		return isOnLimit() && node instanceof Nonterminal;
	}
	
	public GeneratorContext<IterativeNodeInstance> getCtx() {
		return ctx;
	}
	public void setCtx(GeneratorContext<IterativeNodeInstance> ctx) {
		this.ctx = ctx;
	}
	public List<Rule> getApplicableRules() {
		return applicableRules;
	}
	
}
