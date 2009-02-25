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
 * Filename           $RCSfile: MartRegistry.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/12/13 11:38:55 $
 *               by   $Author: davidwithers $
 * Created on 17-Mar-2006
 *****************************************************************/
package org.biomart.martservice;

import java.util.ArrayList;
import java.util.List;

/**
 * The MartRegistry returned by a BioMart web service.
 * 
 * @author David Withers
 */
public class MartRegistry {
	private List<MartURLLocation> martURLLocations = new ArrayList<MartURLLocation>();

	/**
	 * Returns the martURLLocations.
	 * 
	 * @return the martURLLocations.
	 */
	public MartURLLocation[] getMartURLLocations() {
		return martURLLocations
				.toArray(new MartURLLocation[martURLLocations.size()]);
	}

	/**
	 * Adds a martURLLocation to the registry.
	 * 
	 * @param martURLLocations
	 *            the martURLLocations to add.
	 */
	public void addMartURLLocation(MartURLLocation martURLLocation) {
		martURLLocations.add(martURLLocation);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((martURLLocations == null) ? 0 : martURLLocations.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MartRegistry other = (MartRegistry) obj;
		if (martURLLocations == null) {
			if (other.martURLLocations != null)
				return false;
		} else if (!martURLLocations.equals(other.martURLLocations))
			return false;
		return true;
	}

}
