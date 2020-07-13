package mapClient;

/**
 * Eccezione personalizzata che estende la classe Exception
 * @author Nazar Chekalin
 *
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServerException(String result) {
		super(result);
	}
}
