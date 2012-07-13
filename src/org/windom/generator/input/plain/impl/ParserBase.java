package org.windom.generator.input.plain.impl;

import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Scanner;
import org.windom.generator.input.plain.symbol.Sigil;
import org.windom.generator.input.plain.symbol.Tag;
import org.windom.generator.input.plain.symbol.Token;

public class ParserBase {

	private final Scanner scanner;
	private Token look;
	
	public ParserBase(Scanner scanner) throws InputException {
		this.scanner = scanner;
		this.step();
	}
	
	protected void step() throws InputException {
		look = scanner.token();
	}
	
	protected boolean matches(Tag tag) {
		return look.getTag() == tag;
	}
	
	protected boolean matches(Token token) {
		return look.equals(token);
	}
	
	protected boolean matches(char character) {
		return matches(new Sigil(character));
	}
	
	protected Token match(Tag tag) throws InputException {
		if (matches(tag)) {
			Token oldlook = look;
			step();
			return oldlook;
		} else {
			expected(tag.toString());
			return null;
		}
	}
	
	protected Token match(Token token) throws InputException {
		if (matches(token)) {
			Token oldlook = look;
			step();
			return oldlook;
		} else {
			expected(token.toString());
			return null;
		}
	}
	
	protected void match(char character) throws InputException {
		match(new Sigil(character));
	}
	
	protected void expected(String what) throws InputException {
		throw new InputException(String.format(
			"Expected %s but got %s near line %d col %d",
			what,
			look,
			look.getLine(),
			look.getCol()));
	}
	
}
