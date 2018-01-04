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
public class DatasetTest extends TestCase {
	private String attributeName;

	private Attribute attribute;

	private String filterName;

	private String filterValue;

	private Filter filter;

//	private Link link;

	private String datasetName;

	private Dataset dataset;

	private Query query;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		attributeName = "attribute name";
		attribute = new Attribute(attributeName);

		filterName = "filter name";
		filterValue = "filter value";
		filter = new Filter(filterName, filterValue);

//		link = new Link("source", "target", "id");

		datasetName = "dataset name";
		dataset = new Dataset(datasetName);
		
		query = new Query("default");
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.Dataset(String)'
	 */
	public final void testDatasetString() {
		Dataset dataset = new Dataset(datasetName);
		assertEquals("Name should be '" + datasetName + "'", dataset.getName(),
				datasetName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.Dataset(Dataset)'
	 */
	public final void testDatasetDataset() {
		dataset.addAttribute(attribute);
		dataset.addFilter(filter);
		Dataset copy = new Dataset(dataset);
		assertEquals("Name should be '" + datasetName + "'", copy.getName(),
				datasetName);
		assertEquals(copy.getAttributes().size(), 1);
		assertEquals(((Attribute) copy.getAttributes().get(0)).getName(),
				attribute.getName());
		assertEquals(copy.getFilters().size(), 1);
		assertEquals(((Filter) copy.getFilters().get(0)).getName(), filter
				.getName());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.getName()'
	 */
	public final void testGetName() {
		assertEquals("Name should be '" + datasetName + "'", dataset.getName(),
				datasetName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.setName(String)'
	 */
	public final void testSetName() {
		String newName = "new dataset name";
		filter.setName(newName);
		assertEquals("Name should be '" + newName + "'", filter.getName(),
				newName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.getAttributes()'
	 */
	public final void testGetAttributes() {
		assertEquals(dataset.getAttributes().size(), 0);
		dataset.addAttribute(attribute);
		assertEquals(dataset.getAttributes().size(), 1);
		assertEquals(dataset.getAttributes().get(0), attribute);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.addAttribute(Attribute)'
	 */
	public final void testAddAttribute() {
		assertTrue(dataset.addAttribute(attribute));
		assertFalse(dataset.addAttribute(attribute));
		assertEquals(dataset.getAttributes().size(), 1);
		assertEquals(dataset.getAttributes().get(0), attribute);
		query.addDataset(dataset);
		assertTrue(dataset.addAttribute(new Attribute("new attribute")));
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.addAttributes(Attribute[])'
	 */
	public final void testAddAttributes() {
		dataset.addAttributes(new Attribute[] {});
		assertEquals(dataset.getAttributes().size(), 0);
		dataset.addAttributes(new Attribute[] { attribute });
		assertEquals(dataset.getAttributes().size(), 1);
		assertEquals(dataset.getAttributes().get(0), attribute);
		Attribute anotherAttribute = new Attribute("another attribute");
		dataset.addAttributes(new Attribute[] { attribute, anotherAttribute });
		assertEquals(dataset.getAttributes().size(), 2);
		assertEquals(dataset.getAttributes().get(0), attribute);
		assertEquals(dataset.getAttributes().get(1), anotherAttribute);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.hasAttribute(Attribute)'
	 */
	public final void testHasAttribute() {
		assertFalse(dataset.hasAttribute(attribute));
		dataset.addAttribute(attribute);
		assertTrue(dataset.hasAttribute(attribute));
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.hasAttributes()'
	 */
	public final void testHasAttributes() {
		assertFalse(dataset.hasAttributes());
		dataset.addAttribute(attribute);
		assertTrue(dataset.hasAttributes());
		dataset.removeAttribute(attribute);
		assertFalse(dataset.hasAttributes());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.removeAttribute(Attribute)'
	 */
	public final void testRemoveAttribute() {
		assertFalse(dataset.removeAttribute(attribute));
		dataset.addAttribute(attribute);
		assertTrue(dataset.removeAttribute(attribute));
		assertEquals(dataset.getAttributes().size(), 0);
		query.addDataset(dataset);
		dataset.addAttribute(attribute);
		assertTrue(dataset.removeAttribute(attribute));
	}

	public void testRemoveAllAttributes() {
		dataset.removeAllAttributes();
		assertFalse(dataset.hasAttributes());
		dataset.addAttribute(attribute);
		dataset.removeAllAttributes();
		assertFalse(dataset.hasAttributes());
		dataset.addAttributes(new Attribute[] { attribute, new Attribute("new attribute") });
		dataset.removeAllAttributes();
		assertFalse(dataset.hasAttributes());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.getFilters()'
	 */
	public final void testGetFilters() {
		assertEquals(dataset.getFilters().size(), 0);
		dataset.addFilter(filter);
		assertEquals(dataset.getFilters().size(), 1);
		assertEquals(dataset.getFilters().get(0), filter);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.addFilter(Filter)'
	 */
	public final void testAddFilter() {
		assertTrue(dataset.addFilter(filter));
		assertFalse(dataset.addFilter(filter));
		assertEquals(dataset.getFilters().size(), 1);
		assertEquals(dataset.getFilters().get(0), filter);
		query.addDataset(dataset);
		assertTrue(dataset.addFilter(new Filter("new filter")));
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.addFilters(Filter[])'
	 */
	public final void testAddFilters() {
		dataset.addFilters(new Filter[] {});
		assertEquals(dataset.getFilters().size(), 0);
		dataset.addFilters(new Filter[] { filter });
		assertEquals(dataset.getFilters().size(), 1);
		assertEquals(dataset.getFilters().get(0), filter);
		Filter anotherFilter = new Filter("another filter");
		dataset.addFilters(new Filter[] { filter, anotherFilter });
		assertEquals(dataset.getFilters().size(), 2);
		assertEquals(dataset.getFilters().get(0), filter);
		assertEquals(dataset.getFilters().get(1), anotherFilter);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.hasFilter(Filter)'
	 */
	public final void testHasFilter() {
		assertFalse(dataset.hasFilter(filter));
		dataset.addFilter(filter);
		assertTrue(dataset.hasFilter(filter));
		dataset.removeFilter(filter);
		assertFalse(dataset.hasFilters());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Dataset.hasFilters()'
	 */
	public final void testHasFilters() {
		assertFalse(dataset.hasFilters());
		dataset.addFilter(filter);
		assertTrue(dataset.hasFilters());
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Dataset.removeFilter(Filter)'
	 */
	public final void testRemoveFilter() {
		assertFalse(dataset.removeFilter(filter));
		dataset.addFilter(filter);
		assertTrue(dataset.removeFilter(filter));
		assertEquals(dataset.getFilters().size(), 0);
		query.addDataset(dataset);
		dataset.addFilter(filter);
		assertTrue(dataset.removeFilter(filter));
	}

	public void testRemoveAllFilters() {
		dataset.removeAllFilters();
		assertFalse(dataset.hasFilters());
		dataset.addFilter(filter);
		dataset.removeAllFilters();
		assertFalse(dataset.hasFilters());
		dataset.addFilters(new Filter[] { filter, new Filter("new filter") });
		dataset.removeAllFilters();
		assertFalse(dataset.hasFilters());
	}

	public void testGetContainingQuery() {
		assertNull(dataset.getContainingQuery());
		query.addDataset(dataset);
		assertEquals(dataset.getContainingQuery(), query);
	}

	public void testSetContainingQuery() {
		dataset.setContainingQuery(query);
		assertEquals(dataset.getContainingQuery(), query);
		dataset.setContainingQuery(null);
		assertNull(dataset.getContainingQuery());
	}

}
