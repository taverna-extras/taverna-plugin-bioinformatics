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
 * Filename           $RCSfile: MartDatasetTest.java,v $
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
public class MartDatasetTest extends TestCase {

	private String displayName;

	private String name;

	private String type;

	private int initialBatchSize;

	private int maximumBatchSize;

	private boolean visible;

	private String interfaceValue;

	private String modified;

	private MartURLLocation martUrlLocation;

	private MartDataset martDataset;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		displayName = "dataset-display-name";
		name = "dataset-name";
		type = "type";
		initialBatchSize = 1;
		maximumBatchSize = 2;
		visible = true;
		interfaceValue = "interface";
		modified = "modified";
		martUrlLocation = new MartURLLocation();
		martUrlLocation.setDefault(true);
		martUrlLocation.setDisplayName("location-display-name");
		martUrlLocation.setHost("host");
		martUrlLocation.setName("location-name");
		martUrlLocation.setPort(42);
		martUrlLocation.setServerVirtualSchema("server-virtual-schema");
		martUrlLocation.setVirtualSchema("virtual-schema");
		martUrlLocation.setVisible(false);
		martDataset = new MartDataset();
		martDataset.setDisplayName(displayName);
		martDataset.setInitialBatchSize(initialBatchSize);
		martDataset.setMartURLLocation(martUrlLocation);
		martDataset.setMaximumBatchSize(maximumBatchSize);
		martDataset.setName(name);
		martDataset.setType(type);
		martDataset.setVisible(visible);
		martDataset.setInterface(interfaceValue);
		martDataset.setModified(modified);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#hashCode()}.
	 */
	public void testHashCode() {
		assertEquals(martDataset.hashCode(), martDataset.hashCode());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getDisplayName()}.
	 */
	public void testGetDisplayName() {
		assertEquals(martDataset.getDisplayName(), displayName);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setDisplayName(java.lang.String)}.
	 */
	public void testSetDisplayName() {
		martDataset.setDisplayName("new" + displayName);
		assertEquals(martDataset.getDisplayName(), "new" + displayName);
		martDataset.setDisplayName(null);
		assertNull(martDataset.getDisplayName());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getInitialBatchSize()}.
	 */
	public void testGetInitialBatchSize() {
		assertEquals(martDataset.getInitialBatchSize(), initialBatchSize);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setInitialBatchSize(long)}.
	 */
	public void testSetInitialBatchSize() {
		martDataset.setInitialBatchSize(1 + initialBatchSize);
		assertEquals(martDataset.getInitialBatchSize(), 1 + initialBatchSize);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getMaximumBatchSize()}.
	 */
	public void testGetMaximumBatchSize() {
		assertEquals(martDataset.getMaximumBatchSize(), maximumBatchSize);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setMaximumBatchSize(long)}.
	 */
	public void testSetMaximumBatchSize() {
		martDataset.setMaximumBatchSize(1 + maximumBatchSize);
		assertEquals(martDataset.getMaximumBatchSize(), 1 + maximumBatchSize);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#getName()}.
	 */
	public void testGetName() {
		assertEquals(martDataset.getName(), name);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setName(java.lang.String)}.
	 */
	public void testSetName() {
		martDataset.setName("new" + name);
		assertEquals(martDataset.getName(), "new" + name);
		martDataset.setName(null);
		assertNull(martDataset.getName());
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#getType()}.
	 */
	public void testGetType() {
		assertEquals(martDataset.getType(), type);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setType(java.lang.String)}.
	 */
	public void testSetType() {
		martDataset.setType("new" + type);
		assertEquals(martDataset.getType(), "new" + type);
		martDataset.setType(null);
		assertNull(martDataset.getType());
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#isVisible()}.
	 */
	public void testIsVisible() {
		assertEquals(martDataset.isVisible(), visible);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setVisible(boolean)}.
	 */
	public void testSetVisible() {
		martDataset.setVisible(!visible);
		assertEquals(martDataset.isVisible(), !visible);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#getInterface()}.
	 */
	public void testGetInterface() {
		assertEquals(martDataset.getInterface(), interfaceValue);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setInterface(java.lang.String)}.
	 */
	public void testSetInterface() {
		martDataset.setInterface("new" + interfaceValue);
		assertEquals(martDataset.getInterface(), "new" + interfaceValue);
		martDataset.setInterface(null);
		assertNull(martDataset.getInterface());
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#getModified()}.
	 */
	public void testGetModified() {
		assertEquals(martDataset.getModified(), modified);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setModified(java.lang.String)}.
	 */
	public void testSetModified() {
		martDataset.setModified("new" + modified);
		assertEquals(martDataset.getModified(), "new" + modified);
		martDataset.setModified(null);
		assertNull(martDataset.getModified());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getMartURLLocation()}.
	 */
	public void testGetMartURLLocation() {
		assertEquals(martDataset.getMartURLLocation(), martUrlLocation);
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#setMartURLLocation(org.biomart.martservice.MartURLLocation)}.
	 */
	public void testSetMartURLLocation() {
		martUrlLocation = new MartURLLocation();
		martDataset.setMartURLLocation(martUrlLocation);
		assertEquals(martDataset.getMartURLLocation(), martUrlLocation);
		martDataset.setMartURLLocation(null);
		assertNull(martDataset.getMartURLLocation());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getVirtualSchema()}.
	 */
	public void testGetVirtualSchema() {
		assertEquals(martDataset.getVirtualSchema(), martDataset
				.getMartURLLocation().getVirtualSchema());
		martDataset.setMartURLLocation(null);
		assertNull(martDataset.getVirtualSchema());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getQualifiedName()}.
	 */
	public void testGetQualifiedName() {
		assertEquals(martDataset.getQualifiedName(), martDataset
				.getVirtualSchema()
				+ "." + name);
		martDataset.setMartURLLocation(null);
		assertEquals(martDataset.getQualifiedName(), name);
	}

	/**
	 * Test method for {@link org.biomart.martservice.MartDataset#toString()}.
	 */
	public void testToString() {
		assertEquals(martDataset.toString(), martDataset.getDisplayName());
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		MartDataset martDataset2 = new MartDataset();
		martDataset2.setDisplayName(displayName);
		martDataset2.setInitialBatchSize(initialBatchSize);
		martDataset2.setMartURLLocation(martUrlLocation);
		martDataset2.setMaximumBatchSize(maximumBatchSize);
		martDataset2.setName(name);
		martDataset2.setType(type);
		martDataset2.setVisible(visible);

		assertTrue(martDataset.equals(martDataset));
		assertTrue(martDataset.equals(martDataset2));
		assertTrue(martDataset2.equals(martDataset));
		assertFalse(martDataset.equals(null));
		assertFalse(martDataset.equals(new Object()));
		assertFalse(martDataset.equals(new MartRegistry()));
		martDataset2.setName("new" + name);
		assertFalse(martDataset.equals(martDataset2));
		assertFalse(martDataset2.equals(martDataset));
	}

	/**
	 * Test method for
	 * {@link org.biomart.martservice.MartDataset#getDisplayComparator()}.
	 */
	public void testGetDisplayComparator() {
		MartDataset martDataset2 = new MartDataset();
		martDataset2.setDisplayName(displayName);
		assertEquals(MartDataset.getDisplayComparator().compare(martDataset, martDataset2), 0);
		martDataset2.setDisplayName("new" + displayName);
		assertTrue(MartDataset.getDisplayComparator().compare(martDataset, martDataset2) != 0);
	}

}
