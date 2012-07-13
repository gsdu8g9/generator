package org.windom.generator.input.plain.symbol;

public class Literal extends Token {

	private final String text;
	
	public Literal(String text) {
		super(Tag.LITERAL);
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "literal '" + text + "'";
	}
	
	public String getText() {
		return text;
	}
	
}
