package database;

public class EmptySetException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptySetException() {

	}

	public EmptySetException(String msg) {
		super(msg);
	}
}
