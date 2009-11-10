/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

public class BiomobyActivityContextualViewFactory implements ContextualViewFactory<BiomobyActivity>{

	public boolean canHandle(Object activity) {
		return activity instanceof BiomobyActivity;
	}

	public List<ContextualView> getViews(BiomobyActivity activity) {
		return Arrays.asList(new ContextualView[] {new BiomobyActivityContextualView(activity)});
	}
}
