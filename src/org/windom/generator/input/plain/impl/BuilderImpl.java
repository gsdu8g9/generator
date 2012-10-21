package org.windom.generator.input.plain.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Definition;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Builder;

public class BuilderImpl implements Builder {
	
	private static final Logger log = LoggerFactory.getLogger(BuilderImpl.class);
	
	private final Map<String,Symbol> symbolMap = new HashMap<String,Symbol>();
	private final Map<String,Annotated> annotatedMap = new HashMap<String,Annotated>();
	private Node start = null;
	
	private static final String PHANTOM_SYMBOL_MASK = "#%d";
	private int phantomSymbolCount = 0;
	
	@Override
	public Definition build() throws InputException {
		if (start == null) throw new InputException("No symbols defined");
		completeTags();
		Definition definition = new Definition(start);
		checkDefinition(definition);
		return definition;
	}
	
	@Override
	public Symbol buildSymbol(Symbol left, List<Rule> rightSides) throws InputException {
		Symbol symbol = resolveSymbol(left);
		if (rightSides.size() == 0) {
			rightSides.add(new Rule(0));
		}
		for (Rule rightSide : rightSides) {
			List<Node> right = rightSide.getRight();
			for (int i=0; i<right.size(); i++) {
				right.set(i,resolveNode(right.get(i)));
			}
			Rule rule = new Rule(rightSide.getProbability(), symbol, right);
			if (!symbol.getRules().contains(rule)) {
				symbol.getRules().add(rule);
			} else {
				log.warn("Ignoring duplicate rule: {}",rule);
			}
		}
		if (start == null && left != null) {
			start = symbol;
		}
		return symbol;
	}
	
	private Node resolveNode(Node node) {
		if (node instanceof Annotated) {
			return resolveAnnotated((Annotated) node);
		} else if (node instanceof Symbol) {
			return resolveSymbol((Symbol) node);
		} else {
			return node;
		}
	}
	
	private Annotated resolveAnnotated(Annotated annotated) {
		String name = annotated.getName();
		if (annotatedMap.containsKey(name)) {
			annotated = annotatedMap.get(name);
		} else {
			annotated.setNode(resolveNode(annotated.getNode()));
			annotatedMap.put(name, annotated);
		}
		return annotated;
	}

	private Symbol resolveSymbol(Symbol symbol) {
		if (symbol == null) {
			return makePhantomSymbol();
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
