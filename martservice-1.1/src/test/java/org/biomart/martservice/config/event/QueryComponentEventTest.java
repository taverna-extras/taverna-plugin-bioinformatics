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
 * Filename           $RCSfile: QueryComponentEventTest.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:15 $
 *               by   $Author: davidwithers $
 * Created on 06-Jun-2006
 *****************************************************************/
package org.biomart.martservice.config.event;

import org.biomart.martservice.MartDataset;

import junit.framework.TestCase;

/**
 * 
 * @author David Withers
 */
public class QueryComponentEventTest extends TestCase {
	private QueryComponentEvent queryComponentEvent;

	private QueryComponentEvent nullQueryComponentEvent;

	private Object source;

	private String name;

	private MartDataset martDataset;

	private String value;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		source = "source";
		name = "name";
		martDataset = new MartDataset();
		value = "value";
		queryComponentEvent = new QueryComponentEvent(source, name, martDataset, value);
		nullQueryComponentEvent = new QueryComponentEvent(source, null, null, null);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.config.event.QueryComponentEvent.QueryComponentEvent(Object,
	 * String, MartDataset)'
	 */
	public void testQueryComponentEventObjectStringMartDataset() {
		QueryComponentEvent event = new QueryComponentEvent(source, name, martDataset);
		assertSame(event.getSource(), source);
		assertEquals(event.getName(), name);
		assertEquals(event.getDataset(), martDataset);
		assertNull(event.getValue());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.config.event.QueryComponentEvent.QueryComponentEvent(Object,
	 * String, MartDataset, String)'
	 */
	public void testQueryComponentEventObjectStringMartDatasetString() {
		QueryComponentEvent event = new QueryComponentEvent(source, name, martDataset, value);
		assertSame(event.getSource(), source);
		assertEquals(event.getName(), name);
		assertEquals(event.getDataset(), martDataset);
		assertEquals(event.getValue(), value);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.config.event.QueryComponentEvent.getName()'
	 */
	public void testGetName() {
		assertEquals(queryComponentEvent.getName(), name);
		assertNull(nullQueryComponentEvent.getName());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.config.event.QueryComponentEvent.getDataset()'
	 */
	public void testGetDataset() {
		assertEquals(queryComponentEvent.getDataset(), martDataset);
		assertNull(nullQueryComponentEvent.getDataset());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.config.event.QueryComponentEvent.getValue()'
	 */
	public void testGetValue() {
		assertEquals(queryComponentEvent.getValue(), value);
		assertNull(nullQueryComponentEvent.getValue());
	}

	/*
	 * Test method for 'java.util.EventObject.getSource()'
	 */
	public void testGetSource() {
		assertSame(queryComponentEvent.getSource(), source);
		assertSame(nullQueryComponentEvent.getSource(), source);
	}

}
