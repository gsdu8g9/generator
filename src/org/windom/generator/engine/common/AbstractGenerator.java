package org.windom.generator.engine.common;

import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.engine.Generator;
import org.windom.generator.engine.GeneratorException;
import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.util.IndentedLogger;

public abstract class AbstractGenerator implements Generator {

	protected final IndentedLogger log = new IndentedLogger(getClass(), "  ");
	
	protected final Definition definition;
	protected final Random rng;
	
	public AbstractGenerator(Definition definition, Random rng) {
		this.definition = definition;
		this.rng = rng;
	}
	
	@Override
	public TreeInstance generate() throws GeneratorException {
		GeneratorContext ctx = new GeneratorContext();
		NodeInstance startInstance = generate(
				definition.getStart(),
				ctx);
		log.info("succeeded rules: {} failed rules: {}", 
				ctx.getStats().getSucceededRules(),
				ctx.getStats().getFailedRules());
		if (startInstance == null) {
			throw new GeneratorException("Failed to generate anything");
		}
		return new TreeInstance(startInstance);
	}
	
	protected abstract NodeInstance generate(Node node, GeneratorContext ctx);
	
	protected Rule chooseRule(List<Rule> rules) {
		log.debug("applicable-rules: {}", rules);
		Rule rule = ProbabilityUtil.chooseRule(rules, rng);
		log.debug("chosen-rule: {}", rule);
		return rule;
	}
	
}
