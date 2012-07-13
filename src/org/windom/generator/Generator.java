
package org.windom.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Terminal;
import org.windom.util.Utils;

public class Generator {

	private final Definition definition;
	private final Random rng;

	private boolean formatSentences = true;
	
	public Generator(Definition definition, Random rng) {
		this.definition = definition;
		this.rng = rng;
	}
	
	public Generator(Definition definition) {
		this(definition, new Random());
	}
	
	public boolean isFormatSentences() {
		return formatSentences;
	}
	
	public void setFormatSentences(boolean formatSentences) {
		this.formatSentences = formatSentences;
	}
	
	public String generate() {	
		return postProcess(generate(definition.getStart()));
	}
	
	public String generate(String nodeName) {
		return postProcess(generate(definition.node(nodeName)));
	}
	
	private String postProcess(List<Terminal> terminals) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<terminals.size(); i++) {
			Terminal terminal = terminals.get(i);
			String text = terminal.getText().trim();
			if ("@".equals(text) && i<terminals.size()-1) {
				String nextText = terminals.get(i+1).getText().trim();
				text = getNevelo(nextText);
			} else if ("/".equals(text)) {
				text = "\n ";
			}
			if (i == 0 && formatSentences) text = Utils.capitalize(text);
			if (i > 0 && text.length() > 0) {
				sb.append(' ');
			}
			sb.append(text);
		}
		if (formatSentences) sb.append('.');
		return sb.toString();
	}
	
	private String getNevelo(String noun) {
		if (noun.length() == 0) return "";
		switch (noun.charAt(0)) {
		case 'a': case 'A':
		case 'e': case 'E':
		case 'i': case 'I':
		case 'o': case 'O':
		case 'u': case 'U':
			return "az";
		default:
			return "a";
		}
	}
	
	private List<Terminal> generate(Node node) {
		List<Terminal> terminals = new ArrayList<Terminal>(32);
		generate(node, terminals);
		return terminals;
	}
	
	private void generate(Node node, List<Terminal> terminals) {
		if (node instanceof Terminal) {
			terminals.add((Terminal) node);
		} else {
			Rule rule = chooseRule((Nonterminal) node);
			for (Node rightNode : rule.getRight()) {
				generate(rightNode, terminals);
			}
		}
	}
	
	private Rule chooseRule(Nonterminal node) {
		List<Rule> rules = node.getRules();
		return rules.get(rng.nextInt(rules.size()));
	}
	
}
