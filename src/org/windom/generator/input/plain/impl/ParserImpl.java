package org.windom.generator.input.plain.impl;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Rule;
import org.windom.generator.definition.Symbol;
import org.windom.generator.definition.Terminal;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Builder;
import org.windom.generator.input.plain.Parser;
import org.windom.generator.input.plain.Scanner;
import org.windom.generator.input.plain.symbol.Identifier;
import org.windom.generator.input.plain.symbol.Literal;
import org.windom.generator.input.plain.symbol.Numeric;
import org.windom.generator.input.plain.symbol.Tag;

public class ParserImpl extends ParserBase implements Parser {

	private final Builder builder;
	
	public ParserImpl(Scanner scanner, Builder builder) throws InputException {
		super(scanner);
		this.builder = builder;
	}
	
	@Override
	public void parse() throws InputException {
		while (!matches(Tag.END)) {
			rule();
		}
	}
	
	private void rule() throws InputException {
		Symbol left = symbol();
		match(Tag.EXPAND);
		builder.buildSymbol(left, rightSides());
		match(';');
	}
	
	private List<Rule> rightSides() throws InputException {
		List<Rule> rightSides = new ArrayList<Rule>();
		if (!matches(';') && !matches(')')) {
			rightSides.add(rightSide());
			while (matches('|')) {
				match('|');
				rightSides.add(rightSide());
			}
		}
		return rightSides;
	}
	
	private Rule rightSide() throws InputException {
		Rule rightSide = new Rule(probability());
		while (!matches(';') && !matches('|') && !matches(')')) {
			rightSide.getRight().add(factor());
		}
		return rightSide;
	}
	
	private int probability() throws InputException {
		int probability = 0;
		if (matches('[')) {
			match('[');
			while (matches('!')) {
				match('!');
				probability += Rule.PROBABILITY_PRIORITY_VAL;
			}
			if (probability == 0 || matches(Tag.NUMERIC)) {
				probability += ((Numeric) match(Tag.NUMERIC)).getValue();
			}
			match(']');
		}
		return probability;
	}

	private Node factor() throws InputException {
		Annotation annotation = match(Annotation.ON_NODE);
		return annotation != null
			? new Annotated(annotation, factor())
			: node();
	}
	
	private Node node() throws InputException {
		Node node = null;
		if (matches(Tag.LITERAL)) {
			Literal literal = (Literal) match(Tag.LITERAL);
			node = new Terminal(literal.getText());
		} else if (matches('(')) {
			match('(');
			node = builder.buildSymbol(null, rightSides());
			match(')');
		} else {
			Annotation annotation;
			if ((annotation = match(Annotation.ON_SYMBOL_TERMINAL)) != null) {
				node = new Annotated(annotation, matches(Tag.LITERAL)
						? terminal()
						: symbol());
			} else if ((annotation = match(Annotation.ON_SYMBOL)) != null) {
				node = new Annotated(annotation, symbol());
			} else {
				node = symbol();
			}
		}
		return node;
	}
	
	private Symbol symbol() throws InputException {
		Identifier identifier = (Identifier) match(Tag.IDENTIFIER);
		return new Symbol(identifier.getLexeme());
	}
	
	private Terminal terminal() throws InputException {
		Literal literal = (Literal) match(Tag.LITERAL);
		return new Terminal(literal.getText());
	}
	
	private Annotation match(Annotation[] annotations) throws InputException {
		for (Annotation annotation : annotations) {
			if (matches(annotation.getMark())) {
				match(annotation.getMark());
				return annotation;
			}
		}
		return null;
	}
	
}
