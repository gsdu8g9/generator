package org.windom.generator.engine.common;

import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Rule;

public class ProbabilityUtil {
	
	public static Rule chooseRule(List<Rule> rules, Random rng) {
		int maxPriorityLevel = maxPriorityLevel(rules);
		int[] intervals = projectRuleProbabilities(rules, maxPriorityLevel);
		int index = chooseInterval(intervals, rng);
		return index >= 0 ? rules.get(index) : null;
	}
	
	private static int maxPriorityLevel(List<Rule> rules) {
		int maxProbability = Integer.MIN_VALUE;
		for (Rule rule : rules) {
			if (rule.getProbability() > maxProbability)
				maxProbability = rule.getProbability();
		}
		if (maxProbability < 0) {
			return -1;
		} else {
			return maxProbability / Rule.PROBABILITY_PRIORITY_VAL;
		}
	}
	
	private static int[] projectRuleProbabilities(List<Rule> rules, int priorityLevel) {
		int[] probabilities = new int[rules.size()];
		int priorityVal = Rule.PROBABILITY_PRIORITY_VAL * priorityLevel;
		for (int i=0; i<rules.size(); i++) {
			int probability = rules.get(i).getProbability() - priorityVal + 1;
			probabilities[i] = Math.max(probability, 0);			
		}
		return probabilities;
	}
	
	private static int chooseInterval(int[] sizes, Random rng) {
		if (sizes == null || sizes.length == 0) return -1;
		int[] breakpoints = new int[sizes.length];
		int sum = 0;
		for (int i=0; i<sizes.length; i++) {
			sum = breakpoints[i] = sum + sizes[i];
		}
		int point = 1 + rng.nextInt(sum);
		for (int i=0; ; i++) {
			if (point <= breakpoints[i]) 
				return i;
		}
	}

}
