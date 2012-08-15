package org.windom.generator.input.plain.impl;

import java.util.ArrayList;
import java.util.List;

import org.windom.generator.definition.AnnotatedNonterminal;
import org.windom.generator.definition.Annotation;
import org.windom.generator.definition.Node;
import org.windom.generator.definition.Nonterminal;
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
		Nonterminal left = nonterminal();
		match(Tag.EXPAND);
		builder.buildNode(left, rightSides());
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
	
	private Node annotatedNonterminal(Annotation annotation) throws InputException {
		match(annotation.getMark());
		return new AnnotatedNonterminal(annotation, nonterminal());
	}
	
	private Nonterminal nonterminal() throws InputException {
		Identifier identifier = (Identifier) match(Tag.IDENTIFIER);
		return new Nonterminal(identifier.getLexeme());
	}
	
	private Node node() throws InputException {
		Node node = null;
		if (matches(Tag.LITERAL)) {
			Literal literal = (Literal) match(Tag.LITERAL);
			node = new Terminal(literal.getText());
		} else if (matches('(')) {
			match('(');
			node = builder.buildNode(null, rightSides());
			match(')');
		} else {
			for (Annotation annotation : Annotation.values()) {
				if (matches(annotation.getMark())) {
					node = annotatedNonterminal(annotation);
					break;
				}
			}
			if (node == null) {
				node = nonterminal();
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
