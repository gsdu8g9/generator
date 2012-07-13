package org.windom.generator.input.plain.symbol;

public class Token {

	protected final Tag tag;
	protected int line;
	protected int col;
	
	public Token(Tag tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return tag.name();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (tag != other.tag)
			return false;
		return true;
	}

	public Tag getTag() {
		return tag;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
}
