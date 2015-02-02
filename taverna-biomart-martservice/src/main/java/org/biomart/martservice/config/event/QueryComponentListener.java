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
 * Filename           $RCSfile: QueryComponentListener.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:06 $
 *               by   $Author: davidwithers $
 * Created on 03-Apr-2006
 *****************************************************************/
package org.biomart.martservice.config.event;

import java.util.EventListener;

/**
 * The listener interface for receiving QueryComponent events.
 * 
 * @author David Withers
 */
public interface QueryComponentListener extends EventListener {

	/**
	 * Invoked when a <code>QueryComponent</code> for an attribute is
	 * selected.
	 * 
	 * @param event the query component event
	 */
	public void attributeAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for an attribute is
	 * deselected.
	 * 
	 * @param event the query component event
	 */
	public void attributeRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is selected.
	 * 
	 * @param event the query component event
	 */
	public void filterAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is deselected.
	 * 
	 * @param event the query component event
	 */
	public void filterRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is changed.
	 * 
	 * @param event the query component event
	 */
	public void filterChanged(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a link is selected.
	 * 
	 * @param event the query component event
	 */
	public void linkAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a link is deselected.
	 * 
	 * @param event the query component event
	 */
	public void linkRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a dataset link id is
	 * changed.
	 * 
	 * @param event the query component event
	 */
	public void linkChanged(QueryComponentEvent event);

}
