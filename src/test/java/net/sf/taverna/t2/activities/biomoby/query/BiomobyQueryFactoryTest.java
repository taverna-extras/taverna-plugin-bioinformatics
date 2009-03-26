/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.taverna.t2.partition.Query;
import net.sf.taverna.t2.partition.QueryFactory;
import net.sf.taverna.t2.partition.QueryFactoryRegistry;

import org.junit.Test;

public class BiomobyQueryFactoryTest {

	@Test
	public void testSPI() {
		List<QueryFactory> instances = QueryFactoryRegistry.getInstance().getInstances();
		assertTrue("There should be more than one instance found",instances.size()>0);
		boolean found = false;
		for (QueryFactory spi : instances) {
			if (spi instanceof BiomobyQueryFactory) {
				found=true;
				break;
			}
		}
		assertTrue("A BiomobyQueryFactory should have been found",found);
	}
	
	@Test
	public void testCreateQuery() {
		BiomobyQueryFactory qf = new BiomobyQueryFactory();
		Query<?> q = qf.createQuery("bob");
		assertNotNull("There query must not be null",q);
		assertTrue("The query must be an instanceof BiomobyQuery",q instanceof BiomobyQuery);
	}
	
	@Test
	public void testHasActionHandler() {
		BiomobyQueryFactory qf = new BiomobyQueryFactory();
		assertTrue("Should have an action handler",qf.hasAddQueryActionHandler());
		assertNotNull("The action handler should not be null",qf.getAddQueryActionHandler());
	}
}
