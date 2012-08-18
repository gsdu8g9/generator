package org.windom.generator.definition;

public enum Annotation {

	PERM      ('$'),
	ADD_TAG   ('+'),
	DEL_TAG   ('-'),
	CHECK_TAG ('@'),
	SUCCEEDS  ('?'),
	FAILS     ('!');
	
	public static final Annotation[] ON_NONTERMINAL = new Annotation[] {
		PERM, ADD_TAG, DEL_TAG, CHECK_TAG
	};
	
	public static final Annotation[] ON_RULE = new Annotation[] {
		SUCCEEDS, FAILS
	};
	
	private final char mark;
	
	private Annotation(char mark) {
		this.mark = mark;
	}
	
	public char getMark() {
		return mark;
	}
	
}
