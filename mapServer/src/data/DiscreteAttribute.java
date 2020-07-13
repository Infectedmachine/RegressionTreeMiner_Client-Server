package data;

import java.util.*;
import java.io.Serializable;

/**
 * Estende la classe attributo, rappresenta un attributo discreto.
 * @author Nazar Chekalin
 *
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> values = new TreeSet<String>();

	/**
	 * Costruttore di classe. invoca il costruttore della super-classe, salva l'insieme dei valori. 
	 * @param name - nome attributo
	 * @param index - indice colonna nella tabella
	 * @param values - insieme dei valori assunti
	 */
	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Restituisce il numero dei valori distinti dell'insieme dei valori.
	 * @return Int - intero
	 */
	public int getNumberOfDistinctValues() {
		return this.values.size();
	}

	/**
	 * Iteratore for each
	 */
	public Iterator<String> iterator() {
		return values.iterator();
	}
}
