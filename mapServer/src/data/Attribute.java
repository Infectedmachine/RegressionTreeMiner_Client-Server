package data;

import java.io.Serializable;

/**
 * Modella un attributo generico della tabella degli esempi del training set
 * @author Nazar Chekalin
 *
 */
public abstract class Attribute implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int index;

	/**
	 * Costruttore di classe. Inizializza nome e indice dell'attributo generico. 
	 * @param name - nome attributo
	 * @param index - indice colonna all'interno della tabella
	 */
	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * Restituisce il nome dell'attributo
	 * @return String - Stringa contenente il nome dell'attributo
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Restituisce l'indice dell'attributo
	 * @return Int - Valore di tipo Intero dell'indice
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Crea una stringa contenente il nome dell'attributo
	 */
	public String toString() {
		return this.name;
	}

}
