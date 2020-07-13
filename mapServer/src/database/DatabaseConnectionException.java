package database;

/**
 * Estende la classe Exception, rappresenta un'eccezione personalizzata.
 * @author Nazar Chekalin
 *
 */
public class DatabaseConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException() {

	}

	/**
	 * Costruttore di classe, invoca il costruttore della super-clase
	 * @param msg - Stringa contenente messaggio dell'eccezione. 
	 */
	public DatabaseConnectionException(String msg) {
		super(msg);
	}
}
