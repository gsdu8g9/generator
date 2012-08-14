package org.windom.generator.definition;

public enum Annotation {

	PERM("$");
	
	private final String mark;
	
	private Annotation(String mark) {
		this.mark = mark;
	}
	
	public String getMark() {
		return mark;
	}
	
}
