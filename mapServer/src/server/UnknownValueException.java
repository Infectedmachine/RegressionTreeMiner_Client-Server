package server;

/**
 * Estende la classe Exception, rappresenta un'eccezione personalizzata.
 * @author Nazar Chekalin
 *
 */
public class UnknownValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownValueException() {

	}

	/**
	 * Costruttore di classe, invoca il costruttore della super-classe Exception
	 * @param msg - Stringa contenente il messaggio dell'eccezione
	 */
	public UnknownValueException(String msg) {
		super(msg);
	}
}
