/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import net.sf.taverna.t2.partition.ActivityQuery;

import org.apache.log4j.Logger;
import org.biomoby.shared.MobyException;

public class BiomobyQuery extends ActivityQuery {

	
	private static Logger logger = Logger.getLogger(BiomobyQuery.class);
	private final String uri;
	public BiomobyQuery(String url) {
		this(url,null);
	}
	
	public BiomobyQuery(String url, String uri) {
		super(url);
		this.uri = uri;
	}

	@Override
	public void doQuery() {
		try {
			logger.info("Starting Biomoby query for:"+getProperty());
			BiomobyQueryHelper helper = new BiomobyQueryHelper(getProperty(),uri);
			for (BiomobyActivityItem item : helper.getServices()) {
				add(item);
			}
			
		} catch (MobyException e) {
			logger.error("There was an error querying biomoby",e);
		}
	}

}
