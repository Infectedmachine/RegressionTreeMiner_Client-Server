package data;

/**
 * Estende la classe Exception, rappresenta un'eccezione personalizzata.
 * @author Nazar Chekalin
 *
 */
public class TrainingDataException extends Exception {
	private static final long serialVersionUID = 1L;
	public TrainingDataException() {
		
	}
	/**
	 * Costruttore di classe. invoca il costruttore della super-classe
	 * @param msg - stringa contenente messaggio d'errore
	 */
	public TrainingDataException(String msg) {
		super(msg);
	}
}
