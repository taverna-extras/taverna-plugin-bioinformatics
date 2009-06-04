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

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.actions.MobyObjectDetailsAction;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * @author Stuart Owen
 * 
 */
@SuppressWarnings("serial")
public class BiomobyObjectActivityContextualView extends
		HTMLBasedActivityContextualView<BiomobyObjectActivityConfigurationBean> {

	public BiomobyObjectActivityContextualView(Activity<?> activity) {
		super(activity);
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "<tr><td>Endpoint</td><td>"
				+ getConfigBean().getMobyEndpoint() + "</td></tr>";
		html += "<tr><td>Authority</td><td>"
				+ getConfigBean().getAuthorityName() + "</td></tr>";
		html += "<tr><td>Datatype</td><td>" + getConfigBean().getServiceName()
				+ "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Biomoby Object activity";
	}

	/**
	 * Gets the component from the {@link HTMLBasedActivityContextualView} and
	 * adds buttons to it allowing Moby object details
	 */
	@Override
	public JComponent getMainFrame() {
		final JComponent mainFrame = super.getMainFrame();
		BiomobyObjectActivity activity = (BiomobyObjectActivity) getActivity();
		if (activity.getMobyObject() != null) {
			JPanel flowPanel = new JPanel(new FlowLayout());
			JButton button = new JButton(new MobyObjectDetailsAction(activity,
					null));
			flowPanel.add(button);
			mainFrame.add(flowPanel, BorderLayout.SOUTH);
		}
		return mainFrame;
	}
}
