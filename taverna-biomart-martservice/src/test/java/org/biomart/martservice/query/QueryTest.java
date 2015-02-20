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
 * Filename           $RCSfile: QueryTest.java,v $
 * Revision           $Revision: 1.3 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/10/01 12:11:30 $
 *               by   $Author: davidwithers $
 * Created on 03-May-2006
 *****************************************************************/
package org.biomart.martservice.query;

import junit.framework.TestCase;

/**
 * 
 * @author David Withers
 */
public class QueryTest extends TestCase {
	private String attributeName;

	private Attribute attribute;

	private String filterName;

	private String filterValue;

	private Filter filter;

	private Link link;

	private String datasetName;

	private Dataset dataset;

	private String virtualSchemaName;
	
	private String softwareVersion;
	
	private String formatter;

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

		link = new Link("source", "target", "id");

		datasetName = "dataset name";
		dataset = new Dataset(datasetName);

		dataset.addAttribute(attribute);
		dataset.addFilter(filter);

		virtualSchemaName = "default";
		
		softwareVersion = "software version";
		
		formatter = "page formatter";
		
		query = new Query(virtualSchemaName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.Query(String)'
	 */
	public final void testQueryString() {
		Query query = new Query(virtualSchemaName);
		assertEquals("virtualSchemaName should be '" + virtualSchemaName + "'",
				query.getVirtualSchemaName(), virtualSchemaName);
		assertEquals("count should be '0'", query.getCount(), 0);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.Query(String, int)'
	 */
	public final void testQueryStringInt() {
		Query query = new Query(virtualSchemaName, 1);
		assertEquals("virtualSchemaName should be '" + virtualSchemaName + "'",
				query.getVirtualSchemaName(), virtualSchemaName);
		assertEquals("count should be '1'", query.getCount(), 1);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.Query(Query)'
	 */
	public final void testQueryQuery() {
		query.addDataset(dataset);
		query.addLink(link);
		query.setCount(1);
		query.setUniqueRows(1);
		query.setSoftwareVersion(softwareVersion);
		query.setFormatter(formatter);
		Query copy = new Query(query);
		assertEquals(copy.getDatasets().size(), 1);
		assertEquals(copy.getAttributes().size(), 1);
		assertEquals(copy.getFilters().size(), 1);
		assertEquals(copy.getLinks().size(), 1);
		assertEquals(copy.getCount(), 1);
		assertEquals(copy.getUniqueRows(), 1);
		assertEquals(copy.getSoftwareVersion(), softwareVersion);
		assertEquals(copy.getFormatter(), formatter);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Query.getVirtualSchemaName()'
	 */
	public final void testGetVirtualSchemaName() {
		assertEquals("virtualSchemaName should be '" + virtualSchemaName + "'",
				query.getVirtualSchemaName(), virtualSchemaName);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Query.setVirtualSchemaName(String)'
	 */
	public final void testSetVirtualSchemaName() {
		String newVirtualSchemaName = "new virtual schema name";
		query.setVirtualSchemaName(newVirtualSchemaName);
		assertEquals("virtualSchemaName should be '" + newVirtualSchemaName
				+ "'", query.getVirtualSchemaName(), newVirtualSchemaName);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getCount()'
	 */
	public final void testGetCount() {
		assertEquals("count should be '0'", query.getCount(), 0);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.setCount(int)'
	 */
	public final void testSetCount() {
		query.setCount(1);
		assertEquals("count should be '1'", query.getCount(), 1);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getUniqueRows()'
	 */
	public final void testGetUniqueRows() {
		assertEquals("uniqueRows should be '0'", query.getUniqueRows(), 0);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.setUniqueRows(int)'
	 */
	public final void testSetUniqueRows() {
		query.setUniqueRows(1);
		assertEquals("uniqueRows should be '1'", query.getUniqueRows(), 1);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getSoftwareVersion()'
	 */
	public final void testGetSoftwareVersion() {
		assertNull("softwareVersion should be null", query.getSoftwareVersion());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.setSoftwareVersion(String)'
	 */
	public final void testSetSoftwareVersion() {
		String newSoftwareVersion = "new software version";
		query.setSoftwareVersion(newSoftwareVersion);
		assertEquals("softwareVersion should be '" + newSoftwareVersion + "'", query.getSoftwareVersion(), newSoftwareVersion);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getFormatter()'
	 */
	public final void testGetFormatter() {
		assertNull("formatter should be null", query.getFormatter());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.setFormatter(String)'
	 */
	public final void testSetFormatter() {
		String newFormatter = "new formatter";
		query.setFormatter(newFormatter);
		assertEquals("formatter should be '" + newFormatter + "'", query.getFormatter(), newFormatter);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getRequestId()'
	 */
	public final void testGetRequestId() {
		assertNull("requestId should be null", query.getRequestId());
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.setnewRequestId(String)'
	 */
	public final void testSetRequestId() {
		String newRequestId = "new RequestId";
		query.setRequestId(newRequestId);
		assertEquals("requestId should be '" + newRequestId + "'", query.getRequestId(), newRequestId);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.addDataset(Dataset)'
	 */
	public final void testAddDataset() {
		query.addDataset(dataset);
		assertEquals(query.getDatasets().size(), 1);
		assertSame(query.getDatasets().get(0), dataset);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Query.removeDataset(Dataset)'
	 */
	public final void testRemoveDataset() {
		query.addDataset(dataset);
		query.removeDataset(dataset);
		assertEquals(query.getDatasets().size(), 0);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getDatasets()'
	 */
	public final void testGetDatasets() {
		query.addDataset(dataset);
		assertEquals(query.getDatasets().size(), 1);
		assertSame(query.getDatasets().get(0), dataset);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getDataset(String)'
	 */
	public final void testGetDataset() {
		query.addDataset(dataset);
		assertSame(query.getDataset(dataset.getName()), dataset);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getAttributes()'
	 */
	public final void testGetAttributes() {
		query.addDataset(dataset);
		assertEquals(query.getAttributes().size(), 1);
		assertSame(query.getAttributes().get(0), attribute);
	}

	/*
	 * Test method for 'org.biomart.martservice.query.Query.getFilters()'
	 */
	public final void testGetFilters() {
		query.addDataset(dataset);
		assertEquals(query.getFilters().size(), 1);
		assertSame(query.getFilters().get(0), filter);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Query.addQueryListener(QueryListener)'
	 */
	public final void testAddQueryListener() {
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.query.Query.removeQueryListener(QueryListener)'
	 */
	public final void testRemoveQueryListener() {
	}

}
