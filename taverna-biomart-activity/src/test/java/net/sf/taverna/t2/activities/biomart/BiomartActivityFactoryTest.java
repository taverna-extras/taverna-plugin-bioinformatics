package net.sf.taverna.t2.activities.biomart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author David Withers
 */
public class BiomartActivityFactoryTest {

	private BiomartActivityFactory factory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		factory = new BiomartActivityFactory();
	}

	/**
	 * Test method for {@link net.sf.taverna.t2.activities.biomart.BiomartActivityFactory#createActivity()}.
	 */
	@Test
	public void testCreateActivity() {
		BiomartActivity createActivity = factory.createActivity();
		assertNotNull(createActivity);
	}

	/**
	 * Test method for {@link net.sf.taverna.t2.activities.biomart.BiomartActivityFactory#getActivityType()}.
	 */
	@Test
	public void testGetActivityURI() {
		assertEquals(URI.create(BiomartActivity.URI), factory.getActivityType());
	}

}
