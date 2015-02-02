/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: ResultReceiverException.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008/03/04 16:47:57 $
 *               by   $Author: davidwithers $
 * Created on 05-May-2006
 *****************************************************************/
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
