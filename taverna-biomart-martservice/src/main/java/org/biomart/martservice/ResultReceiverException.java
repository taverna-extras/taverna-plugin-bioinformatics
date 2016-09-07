package org.biomart.martservice;

/**
 *
 * @author David Withers
 */
public class ResultReceiverException extends Exception {
	private static final long serialVersionUID = 7151337259555845771L;

	/**
	 * Constructs a new exception with no detail message.
	 *
	 */
	public ResultReceiverException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause (a null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown)
	 */
	public ResultReceiverException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message
	 *            the detail message
	 */
	public ResultReceiverException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *            the cause (a null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown)
	 */
	public ResultReceiverException(Throwable cause) {
		super(cause);
	}

}
