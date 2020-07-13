package database;

/**
 * Estende la classe Exception, rappresenta un'eccezione personalizzata.
 * @author Nazar Chekalin
 *
 */
public class NoValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoValueException() {

	}

	/**
	 * Costruttore della classe, invoca il costruttore della suepr-classe Exception
	 * @param msg - Stringa contenente il messaggio dell'eccezione
	 */
	public NoValueException(String msg) {
		super(msg);
	}
}
