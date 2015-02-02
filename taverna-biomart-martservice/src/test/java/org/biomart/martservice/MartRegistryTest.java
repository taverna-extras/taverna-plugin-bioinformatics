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
 * Filename           $RCSfile: MartRegistryTest.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:14 $
 *               by   $Author: davidwithers $
 * Created on 4 Aug 2006
 *****************************************************************/
package org.biomart.martservice;

import junit.framework.TestCase;

/**
 *
 * @author David Withers
 */
public class MartRegistryTest extends TestCase {

	private MartURLLocation martUrlLocation;

	private MartRegistry martRegistry;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		martUrlLocation = new MartURLLocation();
		martUrlLocation.setDefault(true);
		martUrlLocation.setDisplayName("location-display-name");
		martUrlLocation.setHost("host");
		martUrlLocation.setName("location-name");
		martUrlLocation.setPort(42);
		martUrlLocation.setServerVirtualSchema("server-virtual-schema");
		martUrlLocation.setVirtualSchema("virtual-schema");
		martUrlLocation.setVisible(false);
		martRegistry = new MartRegistry();
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartRegistry#hashCode()}.
	 */
	public void testHashCode() {
		MartRegistry martRegistry2 = new MartRegistry();
		martRegistry.addMartURLLocation(martUrlLocation);
		martRegistry2.addMartURLLocation(martUrlLocation);
		assertEquals(martRegistry.hashCode(), martRegistry.hashCode());
		assertEquals(martRegistry.hashCode(), martRegistry2.hashCode());
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartRegistry#getMartURLLocations()}.
	 */
	public void testGetMartURLLocations() {
		assertEquals(martRegistry.getMartURLLocations().length, 0);
		martRegistry.addMartURLLocation(martUrlLocation);
		assertEquals(martRegistry.getMartURLLocations()[0], martUrlLocation);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartRegistry#addMartURLLocation(org.biomart.martservice.MartURLLocation)}.
	 */
	public void testAddMartURLLocation() {
		martRegistry.addMartURLLocation(martUrlLocation);
		MartURLLocation[] martURLLocations = martRegistry.getMartURLLocations();
		assertEquals(martURLLocations[martURLLocations.length - 1], martUrlLocation);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartRegistry#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		MartRegistry martRegistry2 = new MartRegistry();
		martRegistry.addMartURLLocation(martUrlLocation);
		martRegistry2.addMartURLLocation(martUrlLocation);

		assertTrue(martRegistry.equals(martRegistry));
		assertTrue(martRegistry.equals(martRegistry2));
		assertTrue(martRegistry2.equals(martRegistry));
		assertFalse(martRegistry.equals(null));
		assertFalse(martRegistry.equals(new Object()));
		assertFalse(martRegistry.equals(new MartRegistry()));
		martRegistry2.addMartURLLocation(martUrlLocation);
		assertFalse(martRegistry.equals(martRegistry2));
		assertFalse(martRegistry2.equals(martRegistry));
	}

}
