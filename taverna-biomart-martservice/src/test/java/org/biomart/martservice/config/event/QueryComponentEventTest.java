/*
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
