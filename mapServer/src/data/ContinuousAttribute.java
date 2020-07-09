package data;

import java.io.Serializable;

public class ContinuousAttribute extends Attribute implements Serializable {
	private static final long serialVersionUID = 1L;

	public ContinuousAttribute(String name, int index) {
		super(name, index);
	}
}
