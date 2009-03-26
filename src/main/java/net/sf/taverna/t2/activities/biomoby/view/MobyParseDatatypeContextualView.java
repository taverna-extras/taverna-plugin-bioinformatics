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

import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivityConfigurationBean;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class MobyParseDatatypeContextualView extends
		HTMLBasedActivityContextualView<MobyParseDatatypeActivityConfigurationBean> {
	
	public MobyParseDatatypeContextualView(Activity<?> activity) {
		super(activity);
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "<tr><td>Article name used by service</td><td>"
				+ getConfigBean().getArticleNameUsedByService() + "</td></tr>";
		html += "<tr><td>Datatype</td><td>"
				+ getConfigBean().getDatatypeName() + "</td></tr>";
		html += "<tr><td>Registry endpoint</td><td>" + getConfigBean().getRegistryEndpoint()
				+ "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Moby parse datatype activity";
	}

}
