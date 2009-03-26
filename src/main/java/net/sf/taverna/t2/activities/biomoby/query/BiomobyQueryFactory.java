/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import net.sf.taverna.t2.activities.biomoby.actions.AddBiomobyRegistryActionHandler;
import net.sf.taverna.t2.partition.ActivityQuery;
import net.sf.taverna.t2.partition.ActivityQueryFactory;
import net.sf.taverna.t2.partition.AddQueryActionHandler;

public class BiomobyQueryFactory extends ActivityQueryFactory {

	@Override
	protected ActivityQuery createQuery(String property) {
		return new BiomobyQuery(property);
	}

	@Override
	protected String getPropertyKey() {
		return "taverna.defaultbiomoby";
	}

	@Override
	public AddQueryActionHandler getAddQueryActionHandler() {
		return new AddBiomobyRegistryActionHandler();
	}

	@Override
	public boolean hasAddQueryActionHandler() {
		return true;
	}
	
	

}
