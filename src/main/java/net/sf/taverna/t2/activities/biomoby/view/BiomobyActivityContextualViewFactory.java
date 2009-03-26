/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.view;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

public class BiomobyActivityContextualViewFactory implements ContextualViewFactory<BiomobyActivity>{

	public boolean canHandle(Object activity) {
		return activity instanceof BiomobyActivity;
	}

	public ContextualView getView(BiomobyActivity activity) {
		return new BiomobyActivityContextualView(activity);
	}
}
