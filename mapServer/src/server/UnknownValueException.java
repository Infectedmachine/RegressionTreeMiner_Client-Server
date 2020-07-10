package server;

public class UnknownValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownValueException() {

	}

	public UnknownValueException(String msg) {
		super(msg);
	}
}
