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
 * Filename           $RCSfile: ResultReceiver.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008/07/31 15:06:49 $
 *               by   $Author: davidwithers $
 * Created on Jan 18, 2008
 *****************************************************************/
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
