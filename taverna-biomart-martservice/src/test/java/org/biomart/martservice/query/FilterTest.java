package org.biomart.martservice.query;

/*
package org.biomart.martservice.query;
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
