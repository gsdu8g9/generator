package org.windom.generator.definition;

import java.util.ArrayList;
import java.util.List;

public class Terminal implements Node {

	private final String text;

	public Terminal(String text) {
		this.text = text;
	}
	
	@Override
	public List<Node> getChildren() {
		return new ArrayList<Node>(0);
	}
	
	@Override
	public String toString() {
		return "'" + text + "'";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Terminal other = (Terminal) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public String getText() {
		return text;
	}
	
}
