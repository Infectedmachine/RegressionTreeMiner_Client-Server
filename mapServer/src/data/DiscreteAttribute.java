package data;

import java.util.*;
import java.io.Serializable;

public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> values = new TreeSet<String>();

	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		this.values = values;
	}

	public int getNumberOfDistinctValues() {
		return this.values.size();
	}

	public Iterator<String> iterator() {
		return values.iterator();
	}
}
