package database;

public class NoValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoValueException() {

	}

	public NoValueException(String msg) {
		super(msg);
	}
}
