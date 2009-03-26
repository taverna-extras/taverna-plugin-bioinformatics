/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BiomobyActivityItemTest {

	@Test
	public void testIcon() {
		BiomobyActivityItem i = new BiomobyActivityItem();
		assertNotNull("The icon must not be null",i.getIcon());
	}
}
