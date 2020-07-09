package data;

import java.io.Serializable;

public abstract class Attribute implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int index;

	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return this.index;
	}

	public String toString() {
		return this.name;
	}

}
