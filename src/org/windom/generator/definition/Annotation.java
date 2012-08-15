package org.windom.generator.definition;

public enum Annotation {

	PERM('$'),
	ADD_TAG('+'),
	DEL_TAG('-'),
	CHECK_TAG('@');
	
	private final char mark;
	
	private Annotation(char mark) {
		this.mark = mark;
	}
	
	public char getMark() {
		return mark;
	}
	
}
