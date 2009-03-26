/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.partition;

import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityItem;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyObjectActivityItem;
import net.sf.taverna.t2.partition.PropertyExtractorSPI;

public class BiomobyPropertyExtractor implements PropertyExtractorSPI {

	public Map<String, Object> extractProperties(Object target) {
		Map<String,Object> map = new HashMap<String, Object>();
		if (target instanceof BiomobyActivityItem) {
			BiomobyActivityItem item = (BiomobyActivityItem)target;
			map.put("type", item.getType());
			map.put("authority", item.getAuthorityName());
			map.put("category", item.getCategory());
			map.put("operation", item.getServiceType());
		}
		if (target instanceof BiomobyObjectActivityItem) {
			BiomobyObjectActivityItem item = (BiomobyObjectActivityItem)target;
			map.put("type", item.getType());
			map.put("authority",item.getAuthorityName());
		}
		return map;
	}

}
