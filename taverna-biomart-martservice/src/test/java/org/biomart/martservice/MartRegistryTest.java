package org.biomart.martservice;

/*
package org.biomart.martservice;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
