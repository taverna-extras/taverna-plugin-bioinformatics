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
 * Filename           $RCSfile: FilterTest.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:10 $
 *               by   $Author: davidwithers $
 * Created on 03-May-2006
 *****************************************************************/
package org.biomart.martservice.query;

import junit.framework.TestCase;

/**
 * 
 * @author David Withers
 */
public class FilterTest extends TestCase {
	private String filterName;

	private String filterValue;

	private Dataset dataset;

	private Filter filter;

	private Query query;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		filterName = "filter name";
		filterValue = "filter value";
		dataset = new Dataset("dataset name");
		filter = new Filter(filterName, filterValue);
		query = new Query("default");
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.Filter(String)'
	 */
	public final void testFilterString() {
		Filter filter = new Filter(filterName);
		assertEquals("Name should be '" + filterName + "'", filter.getName(),
				filterName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.Filter(String,
	 * String)'
	 */
	public final void testFilterStringString() {
		Filter filter = new Filter(filterName, filterValue);
		assertEquals("Name should be '" + filterName + "'", filter.getName(),
				filterName);
		assertEquals("Value should be '" + filterValue + "'",
				filter.getValue(), filterValue);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.Filter(String,
	 * String, boolean)'
	 */
	public final void testFilterStringStringBoolean() {
		Filter filter = new Filter(filterName, filterValue, true);
		assertEquals("Name should be '" + filterName + "'", filter.getName(),
				filterName);
		assertEquals("Value should be '" + filterValue + "'",
				filter.getValue(), filterValue);
		assertTrue("isBoolean should be true", filter.isBoolean());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.Filter(Filter)'
	 */
	public final void testFilterFilter() {
		filter.setContainingDataset(dataset);
		Filter copy = new Filter(filter);
		assertEquals("Name should be '" + filterName + "'", copy.getName(),
				filterName);
		assertEquals("Value should be '" + filterValue + "'", copy.getValue(),
				filterValue);
		assertFalse("isBoolean should be false", copy.isBoolean());
		assertNull(copy.getContainingDataset());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Filter.getContainingDataset()'
	 */
	public final void testGetContainingDataset() {
		assertNull("Default should be NULL ", filter.getContainingDataset());
		dataset.addFilter(filter);
		assertEquals(filter.getContainingDataset(), dataset);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Filter.setContainingDataset(Dataset)'
	 */
	public final void testSetContainingDataset() {
		filter.setContainingDataset(dataset);
		assertEquals(filter.getContainingDataset(), dataset);
		filter.setContainingDataset(null);
		assertNull(filter.getContainingDataset());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.getName()'
	 */
	public final void testGetName() {
		assertEquals("Name should be '" + filterName + "'", filter.getName(),
				filterName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.setName(String)'
	 */
	public final void testSetName() {
		String newName = "new filter name";
		filter.setName(newName);
		assertEquals("Name should be '" + newName + "'", filter.getName(),
				newName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.getValue()'
	 */
	public final void testGetValue() {
		assertEquals("Value should be '" + filterValue + "'",
				filter.getValue(), filterValue);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.setValue(String)'
	 */
	public final void testSetValue() {
		String newValue = "new filter value";
		filter.setValue(newValue);
		assertEquals("Value should be '" + newValue + "'", filter.getValue(),
				newValue);
		filter.setValue(null);
		assertNull(filter.getValue());
		filter.setValue(null);
		assertNull(filter.getValue());
		filter.setValue(newValue);
		assertEquals("Value should be '" + newValue + "'", filter.getValue(),
				newValue);
		filter.setValue(newValue);
		assertEquals("Value should be '" + newValue + "'", filter.getValue(),
				newValue);
		dataset.addFilter(filter);
		filter.setValue(null);
		filter.setValue(newValue);
		assertEquals("Value should be '" + newValue + "'", filter.getValue(),
				newValue);
		query.addDataset(dataset);
		filter.setValue(null);
		filter.setValue(newValue);
		assertEquals("Value should be '" + newValue + "'", filter.getValue(),
				newValue);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.isBoolean()'
	 */
	public final void testIsBoolean() {
		assertFalse("Default should be false", filter.isBoolean());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Filter.setBoolean(boolean)'
	 */
	public final void testSetBoolean() {
		filter.setBoolean(true);
		assertTrue("isBoolean should be true", filter.isBoolean());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Filter.getQualifiedName()'
	 */
	public final void testGetQualifiedName() {
		assertEquals("Qualified name should be '" + filterName + "'", filter
				.getQualifiedName(), filterName);

		String qualifiedName = dataset.getName() + "." + filterName;
		filter.setContainingDataset(dataset);
		assertEquals("Qualified name should be '" + qualifiedName + "'", filter
				.getQualifiedName(), qualifiedName);

		dataset.setName("new dataset name");
		qualifiedName = dataset.getName() + "." + filterName;
		filter.setContainingDataset(dataset);
		assertEquals("Qualified name should be '" + qualifiedName + "'", filter
				.getQualifiedName(), qualifiedName);

		filter.setContainingDataset(null);
		assertEquals("Qualified name should be '" + filterName + "'", filter
				.getQualifiedName(), filterName);
	}

	public void testIsList() {
		assertFalse(filter.isList());
		filter.setList(true);
		assertTrue(filter.isList());
	}

	public void testSetList() {
		filter.setList(false);
		assertFalse(filter.isList());
		filter.setList(true);
		assertTrue(filter.isList());
	}

}
