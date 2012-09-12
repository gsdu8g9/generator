package org.windom.generator.definition;

public enum Annotation {

	PERM      ('$'),
	ADD_TAG   ('+'),
	DEL_TAG   ('-'),
	CHECK_TAG ('@'),
	SUCCEEDS  ('?'),
	FAILS     ('!');
	
	public static final Annotation[] ON_NODE = new Annotation[] {
		SUCCEEDS, FAILS
	};
	
	public static final Annotation[] ON_SYMBOL_TERMINAL = new Annotation[] {
		ADD_TAG, DEL_TAG, CHECK_TAG
	};
	
	public static final Annotation[] ON_SYMBOL = new Annotation[] {
		PERM
	};
	
	private final char mark;
	
	private Annotation(char mark) {
		this.mark = mark;
	}
	
	public char getMark() {
		return mark;
	}
	
}
