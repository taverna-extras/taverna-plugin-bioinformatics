/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.util.List;

import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.visit.VisitReport;

import net.sf.taverna.t2.workflowmodel.health.RemoteHealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.DisabledActivity;

/**
 * A health checker for the Biomoby activity.
 * 
 * @author David Withers
 */
public class BiomobyActivityHealthChecker extends RemoteHealthChecker {
	
	public boolean canVisit(Object subject) {
		if (subject == null) {
			return false;
		}
		if (subject instanceof BiomobyActivity) {
			return true;
		}
		if (subject instanceof DisabledActivity) {
			return (((DisabledActivity) subject).getActivity() instanceof BiomobyActivity);
		}
		return false;
	}

	public VisitReport visit(Object o, List<Object> ancestors) {
		Activity activity = (Activity) o;
		BiomobyActivityConfigurationBean configuration = null;
		if (activity instanceof BiomobyActivity) {
			configuration = (BiomobyActivityConfigurationBean) activity.getConfiguration();
		} else if (activity instanceof DisabledActivity) {
			configuration = (BiomobyActivityConfigurationBean) ((DisabledActivity) activity).getActivityConfiguration();
		}
		return contactEndpoint(activity, configuration.getMobyEndpoint());
	}
}
