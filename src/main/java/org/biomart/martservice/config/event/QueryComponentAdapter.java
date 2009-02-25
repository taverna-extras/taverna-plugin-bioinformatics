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
 * Filename           $RCSfile: QueryComponentAdapter.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:06 $
 *               by   $Author: davidwithers $
 * Created on 04-Apr-2006
 *****************************************************************/
package org.biomart.martservice.config.event;

/**
 * An abstract adapter class for receiving <code>QueryComponent</code> events.
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 * 
 * @author David Withers
 */
public class QueryComponentAdapter implements QueryComponentListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterChanged(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterChanged(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkAdded(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkRemoved(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biomart.martservice.config.QueryComponentListener#linkChanged(org.biomart.martservice.config.QueryComponentEvent)
	 */
	public void linkChanged(QueryComponentEvent event) {
	}

}
