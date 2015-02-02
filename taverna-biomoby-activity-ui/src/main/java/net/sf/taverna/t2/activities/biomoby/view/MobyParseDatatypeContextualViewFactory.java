/**
 * Copyright (C) 2007 The University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
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
