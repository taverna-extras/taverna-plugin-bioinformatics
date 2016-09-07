package org.biomart.martservice;

/**
 * Thrown when a MartService is inaccessable or returns an error.
 *
 * @author David Withers
 */
public class MartServiceException extends Exception {
	private static final long serialVersionUID = 5535008907746588537L;

	/**
	 * Constructs a new exception with no detail message.
	 *
	 */
	public MartServiceException() {
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
	public MartServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message
	 *            the detail message
	 */
	public MartServiceException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *            the cause (a null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown)
	 */
	public MartServiceException(Throwable cause) {
		super(cause);
	}

}
