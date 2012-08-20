package org.windom.generator.input.plain.symbol;

public class Numeric extends Token {

	private final int value;
	
	public Numeric(int value) {
		super(Tag.NUMERIC);
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
