package org.windom.generator.engine.iterative;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.engine.common.GeneratorContext;

public class IterativeNodeInstance extends LinkedNodeInstance<IterativeNodeInstance> {

	private final GeneratorContext<IterativeNodeInstance> ctx;
	private final List<Rule> applicableRules;
	
	public IterativeNodeInstance(Node left, 
			GeneratorContext<IterativeNodeInstance> ctx) {
		super(left);
		this.ctx = ctx;
		this.applicableRules = (left.symbol() != null)
				? new ArrayList<Rule>(left.symbol().getRules())
				: null;
	}
	
	public GeneratorContext<IterativeNodeInstance> getCtx() {
		return ctx;
	}
	public List<Rule> getApplicableRules() {
		return applicableRules;
	}
	
}
