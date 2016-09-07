/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivity;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

/**
 * @author Stuart Owen
 *
 */
public class MobyParseDatatypeContextualViewFactory implements
		ContextualViewFactory<MobyParseDatatypeActivity> {

	private ColourManager colourManager;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory#canHandle
	 * (java.lang.Object)
	 */
	public boolean canHandle(Object activity) {
		return activity instanceof MobyParseDatatypeActivity;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory#getView
	 * (java.lang.Object)
	 */
	public List<ContextualView> getViews(MobyParseDatatypeActivity activity) {
		return Arrays.asList(new ContextualView[] { new MobyParseDatatypeContextualView(activity,
				colourManager) });
	}

	public void setColourManager(ColourManager colourManager) {
		this.colourManager = colourManager;
	}

}
