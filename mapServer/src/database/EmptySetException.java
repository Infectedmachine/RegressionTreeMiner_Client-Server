package database;

/**
 * Estende la classe Exception, rappresenta un'eccezione personalizzata.
 * @author Nazar Chekalin
 *
 */
public class EmptySetException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptySetException() {

	}

	/**
	 * Costruttore di classe, invoca il costruttore della super-classe Exception
	 * @param msg - Stringa contenente il messaggio dell'eccezione
	 */
	public EmptySetException(String msg) {
		super(msg);
	}
}
