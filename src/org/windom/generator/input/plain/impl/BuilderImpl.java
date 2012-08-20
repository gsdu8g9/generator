package org.windom.generator.input.plain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.definition.Terminal;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Builder;
import org.windom.generator.input.plain.PlainInput;

public class BuilderImpl implements Builder {
	
	private static final Logger log = LoggerFactory.getLogger(BuilderImpl.class);
	
	private final Map<String,Symbol> symbolMap = new HashMap<String,Symbol>();
	private final Map<String,Annotated> annotatedMap = new HashMap<String,Annotated>();
	private Nonterminal start = null;
	
	private static final String PHANTOM_SYMBOL_MASK = "#%d";
	private int phantomSymbolCount = 0;
	
	private static final String 
			META_PREFIX  = "__",
			META_ECHO    = "echo",
			META_INCLUDE = "include";
	
	private final Set<String> includedResources = new HashSet<String>();

	public BuilderImpl(String resourceName) {
		if (resourceName != null) {
			includedResources.add(resourceName);
		}
	}
	
	@Override
	public Definition build() throws InputException {
		if (start == null) throw new InputException("No symbols defined");
		completeTags();
		Definition definition = new Definition(start);
		checkDefinition(definition);
		return definition;
	}
	
	@Override
	public Symbol buildSymbol(Symbol left, List<List<Node>> rights) throws InputException {
		Symbol symbol = resolveSymbol(left);
		if (rights.size() == 0) {
			rights.add(new ArrayList<Node>());
		}
		for (List<Node> right : rights) {
			for (int i=0; i<right.size(); i++) {
				Node rightNode = right.get(i);
				if (isMetaNode(rightNode)) {
					throw new InputException(
						"Meta symbol found on right-side of a rule: " + rightNode);
				}
				right.set(i,resolveNode(rightNode));
			}
			Rule rule = new Rule(symbol, right);
			if (!symbol.getRules().contains(rule) || isMetaNode(symbol)) {
				symbol.getRules().add(rule);
			} else {
				log.warn("Ignoring duplicate rule: {}",rule);
			}
		}
		if (isMetaNode(symbol)) {
			handleMetaSymbol(symbol);
			return null;
		}
		if (start == null && left != null) {
			start = symbol;
		}
		return symbol;
	}
	
	private Node resolveNode(Node node) {
		if (node instanceof Nonterminal) {
			return resolveNonterminal((Nonterminal) node);
		} else {
			return node;
		}
	}
	
	private Nonterminal resolveNonterminal(Nonterminal nonterminal) {
		if (nonterminal instanceof Annotated) {
			return resolveAnnotated((Annotated) nonterminal);
		} else {
			return resolveSymbol((Symbol) nonterminal);
		}
	}
	
	private Annotated resolveAnnotated(Annotated annotated) {
		String name = annotated.getName();
		if (annotatedMap.containsKey(name)) {
			annotated = annotatedMap.get(name);
		} else {
			annotated.setNonterminal(resolveNonterminal(annotated.getNonterminal()));
			annotatedMap.put(name, annotated);
		}
		return annotated;
	}

	private Symbol resolveSymbol(Symbol symbol) {
		if (symbol == null) {
			return makePhantomSymbol();
		} else if (isMetaNode(symbol)) {
			return symbol;
		} else if (symbolMap.containsKey(symbol.getName())) {
			return symbolMap.get(symbol.getName());
		} else {
			symbolMap.put(symbol.getName(), symbol);
			return symbol;
		}
	}
	
	private Symbol makePhantomSymbol() {
		return new Symbol(String.format(PHANTOM_SYMBOL_MASK, phantomSymbolCount++));
	}

	private boolean isMetaNode(Node node) {
		return node.symbol() != null &&
				node.symbol().getName().startsWith(META_PREFIX);
	}
	
	private void handleMetaSymbol(Symbol symbol) throws InputException {
		String metaOp = symbol.getName().substring(META_PREFIX.length());
		if (META_ECHO.equals(metaOp)) {
			StringBuilder sb = new StringBuilder();
			for (Rule rule : symbol.getRules()) {
				sb.append('\n');
				boolean firstNode = true;
				for (Node rightNode : rule.getRight()) {
					if (firstNode) {
						firstNode = false;
					} else {
						sb.append(' ');
					}
					if (rightNode instanceof Terminal) {
						sb.append(((Terminal) rightNode).getText());
					} else {
						sb.append(rightNode);
					}
				}
			}
			log.info("{}", sb.toString().replaceAll("(\\r?\\n)", "$1\t## "));
		} else if (META_INCLUDE.equals(metaOp)) {
			for (Rule rule : symbol.getRules()) {
				for (Node rightNode : rule.getRight()) {
					if (rightNode instanceof Terminal) {
						include(((Terminal) rightNode).getText());
					} else {
						log.warn("Ignoring nonterminal in include rule: {}", rightNode);
					}
				}
			}
		} else {
			log.warn("Unrecognized meta symbol: {}", metaOp);
		}
	}
	
	private void include(String resouceName) throws InputException {
		if (!includedResources.contains(resouceName)) {
			includedResources.add(resouceName);
			new PlainInput(resouceName).read(this);
		} else {
			log.debug("{} is already included", resouceName);
		}
	}
	
	private void completeTags() {
		for (Symbol symbol : symbolMap.values()) {
			if (symbol.getRules().isEmpty() && (
				annotatedMap.containsKey(Annotated.getName(Annotation.ADD_TAG, symbol)) ||
				annotatedMap.containsKey(Annotated.getName(Annotation.DEL_TAG, symbol)) ||
				annotatedMap.containsKey(Annotated.getName(Annotation.CHECK_TAG, symbol))
					)) {
				symbol.getRules().add(new Rule(symbol));
			}
		}
	}
	
	private void checkDefinition(Definition definition) throws InputException {
		Collection<Symbol> accessibleSymbols = definition.symbols();
		//
		// Check accessibility
		//
		for (Symbol symbol : symbolMap.values()) {
			if (!accessibleSymbols.contains(symbol)) {
				log.warn("Unreachable symbol: {}", symbol);
			}
		}
		//
		// Check completeness
		//
		for (Symbol symbol : accessibleSymbols) {
			if (symbol.getRules().isEmpty()) {
				throw new InputException("Incomplete symbol: " + symbol);
			}
		}
		//
		// TODO check termination
		//
	}
	
}
