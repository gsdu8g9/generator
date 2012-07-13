package org.windom.generator.definition.traversal;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;

public class PossibilityCounter implements Visitor, Evaluator {

	private final Map<Nonterminal,BigInteger> nodePossibilities;
	private final Map<Rule,BigInteger> rulePossibilities;
	
	public PossibilityCounter() {
		nodePossibilities = new HashMap<Nonterminal,BigInteger>();
		rulePossibilities = new HashMap<Rule,BigInteger>();
	}
	
	@Override
	public boolean prune(Nonterminal node) {
		boolean seen = nodePossibilities.containsKey(node);
		if (!seen) {
			nodePossibilities.put(node, BigInteger.ZERO);
		}
		return seen;
	}
	
	@Override
	public void visit(Nonterminal node) {
		BigInteger nodePoss = BigInteger.ZERO;
		for (Rule rule : node.getRules()) {
			BigInteger rulePoss = BigInteger.ONE;
			for (Node right : rule.getRight()) {
				if (right instanceof Nonterminal) {
					Node rightNode = (Nonterminal) right;
					rulePoss = rulePoss.multiply(nodePossibilities.get(rightNode));
				}
			}
			rulePossibilities.put(rule, rulePoss);
			nodePoss = nodePoss.add(rulePoss);
		}
		nodePossibilities.put(node, nodePoss);
	}
	
	public Map<Nonterminal, BigInteger> getNodePossibilities() {
		return nodePossibilities;
	}
	
	public Map<Rule, BigInteger> getRulePossibilities() {
		return rulePossibilities;
	}
	
}
