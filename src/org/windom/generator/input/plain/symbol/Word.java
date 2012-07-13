package org.windom.generator.input.plain.symbol;

public class Word extends Token {

	private final String lexeme;
	
	public Word(Tag tag, String lexeme) {
		super(tag);
		this.lexeme = lexeme;
	}

	@Override
	public String toString() {
		return "word \"" + lexeme + "\"";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
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
		Word other = (Word) obj;
		if (lexeme == null) {
			if (other.lexeme != null)
				return false;
		} else if (!lexeme.equals(other.lexeme))
			return false;
		return true;
	}

	public String getLexeme() {
		return lexeme;
	}
	
}
