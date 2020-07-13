package data;

import java.io.Serializable;

/**
 * Estende la classe Attributo, rappresenta un attributo continuo
 * @author Nazar Chekalin
 *
 */
public class ContinuousAttribute extends Attribute implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe, invoca il costruttore della super-classe
	 * @param name - nome Attributo Continuo
	 * @param index - indice colonna all'interno della tabella
	 */
	public ContinuousAttribute(String name, int index) {
		super(name, index);
	}
}
