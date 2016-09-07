/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
