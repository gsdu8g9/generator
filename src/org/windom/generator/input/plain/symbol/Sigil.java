package org.windom.generator.input.plain.symbol;

public class Sigil extends Token {

	private final char character;
	
	public Sigil(char character) {
		super(Tag.SIGIL);
		this.character = character;
	}
	
	@Override
	public String toString() {
		return "character '" + character + "'";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + character;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sigil other = (Sigil) obj;
		if (character != other.character)
			return false;
		return true;
	}

	public char getCharacter() {
		return character;
	}
	
}
