package org.windom.generator.input.plain.impl;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.Annotated;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Symbol;
import org.windom.generator.definition.Terminal;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Builder;
import org.windom.generator.input.plain.Parser;
import org.windom.generator.input.plain.Scanner;
import org.windom.generator.input.plain.symbol.Identifier;
import org.windom.generator.input.plain.symbol.Literal;
import org.windom.generator.input.plain.symbol.Tag;

public class ParserImpl extends ParserBase implements Parser {

	private final Builder builder;
	
	public ParserImpl(Scanner scanner, Builder builder) throws InputException {
		super(scanner);
		this.builder = builder;
	}
	
	private void rule() throws InputException {
		Symbol left = symbol();
		match(Tag.EXPAND);
		builder.buildSymbol(left, rightSides());
		match(';');
	}
	
	private List<List<Node>> rightSides() throws InputException {
		List<List<Node>> rights = new ArrayList<List<Node>>();
		if (!matches(';') && !matches(')')) {
			rights.add(rightSide());
			while (matches('|')) {
				match('|');
				rights.add(rightSide());
			}
		}
		return rights;
	}
	
	private List<Node> rightSide() throws InputException {
		List<Node> right = new ArrayList<Node>();
		while (!matches(';') && !matches('|') && !matches(')')) {
			right.add(node());
		}
		return right;
	}
	
	private Annotated annotated(Annotation annotation) throws InputException {
		match(annotation.getMark());
		return new Annotated(annotation, symbol());
	}
	
	private Symbol symbol() throws InputException {
		Identifier identifier = (Identifier) match(Tag.IDENTIFIER);
		return new Symbol(identifier.getLexeme());
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
			for (Annotation annotation : Annotation.ON_NONTERMINAL) {
				if (matches(annotation.getMark())) {
					node = annotated(annotation);
					break;
				}
			}
			if (node == null) {
				node = symbol();
			}
		}
		return node;
	}
	
	@Override
	public void parse() throws InputException {
		while (!matches(Tag.END)) {
			rule();
		}
	}
	
}
