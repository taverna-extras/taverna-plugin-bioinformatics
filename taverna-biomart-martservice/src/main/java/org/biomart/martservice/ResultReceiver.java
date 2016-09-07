package org.biomart.martservice;

/**
 * An interface for receiving results of a Query.
 *
 * @author David Withers
 */
public interface ResultReceiver {

	/**
	 * Receives a single row from the results of executing a Query.
	 *
	 * This method will be called frequently and should not block.
	 *
	 * @param resultRow
	 */
	public void receiveResult(Object[] resultRow, long index) throws ResultReceiverException;

	/**
	 * Receives an error for a single row from the results of executing a Query.
	 *
	 * This method will be called frequently and should not block.
	 *
	 * @param resultRow
	 */
	public void receiveError(String errorMessage, long index) throws ResultReceiverException;

}
