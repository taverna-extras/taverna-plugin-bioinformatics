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
 * Filename           $RCSfile: QueryListener.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/10/03 15:57:30 $
 *               by   $Author: davidwithers $
 * Created on 03-Apr-2006
 *****************************************************************/
package org.biomart.martservice.query;

/**
 * The listener interface for receiving Query events.
 * 
 * @author David Withers
 */
public interface QueryListener {

	/**
	 * Invoked when an <code>Attribute</code> is added to a <code>Query</code>.
	 * 
	 * @param attribute
	 *            the <code>Attribute</code> added.
	 */
	public void attributeAdded(Attribute attribute, Dataset dataset);

	/**
	 * Invoked when an <code>Attribute</code> is removed from a
	 * <code>Query</code>.
	 * 
	 * @param attribute
	 *            the <code>Attribute</code> removed.
	 */
	public void attributeRemoved(Attribute attribute, Dataset dataset);

	/**
	 * Invoked when a <code>Filter</code> is added to a <code>Query</code>.
	 * 
	 * @param filter
	 *            the <code>Filter</code> added.
	 */
	public void filterAdded(Filter filter, Dataset dataset);

	/**
	 * Invoked when a <code>Filter</code> is removed from a <code>Query</code>.
	 * 
	 * @param filter
	 *            the <code>Filter</code> removed.
	 */
	public void filterRemoved(Filter filter, Dataset dataset);

	/**
	 * Invoked when the value of a <code>Filter</code> is changed.
	 * 
	 * @param filter
	 *            the <code>Filter</code> whose value has changed.
	 */
	public void filterChanged(Filter filter, Dataset dataset);

	/**
	 * Invoked when a formatter is added to a <code>Query</code>.
	 * 
	 * @param formatter
	 *            the formatter added.
	 */
	public void formatterAdded(String formatter);

	/**
	 * Invoked when a formatter is removed from a <code>Query</code>.
	 * 
	 * @param formatter
	 *            the formatter removed.
	 */
	public void formatterRemoved(String formatter);

	/**
	 * Invoked when the value of the formatter is changed.
	 * 
	 * @param filter
	 *            the new value of the formatter.
	 */
	public void formatterChanged(String formatter);

}
