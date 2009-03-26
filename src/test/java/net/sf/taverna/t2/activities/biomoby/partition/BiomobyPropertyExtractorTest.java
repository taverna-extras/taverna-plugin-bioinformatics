/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.partition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityItem;
import net.sf.taverna.t2.partition.PropertyExtractorSPI;
import net.sf.taverna.t2.partition.PropertyExtractorSPIRegistry;

import org.junit.Test;

public class BiomobyPropertyExtractorTest {

	@Test
	public void testSPI() {
		List<PropertyExtractorSPI> instances = PropertyExtractorSPIRegistry.getInstance().getInstances();
		assertTrue("There should be more than one instance found",instances.size()>0);
		boolean found = false;
		for (PropertyExtractorSPI spi : instances) {
			if (spi instanceof BiomobyPropertyExtractor) {
				found=true;
				break;
			}
		}
		assertTrue("A BiomobyPropertyExtractor should have been found",found);
	}
	
	@Test
	public void testExtractProperties() {
		BiomobyActivityItem item = new BiomobyActivityItem();
		item.setAuthorityName("The BOSS");
		
		Map<String,Object> props = new BiomobyPropertyExtractor().extractProperties(item);

		assertEquals("missing or incorrect property","Biomoby",props.get("type"));
		assertEquals("missing or incorrect property","The BOSS",props.get("authority"));
	}
}
