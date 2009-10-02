/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

import net.sf.taverna.t2.activities.biomart.BiomartActivity;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;

import org.jdom.Element;

public class BiomartActivityConfigurationAction extends ActivityConfigurationAction<BiomartActivity, Element> {

	private static final long serialVersionUID = 3782223454010961660L;
	private final Frame owner;
	public static final String CONFIGURE_BIOMART = "Configure Biomart query";

	public BiomartActivityConfigurationAction(BiomartActivity activity,Frame owner) {
		super(activity);
		putValue(Action.NAME, CONFIGURE_BIOMART);
		this.owner = owner;
	}

	@SuppressWarnings("serial")
	public void actionPerformed(ActionEvent action) {
		JDialog currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}

		final BiomartConfigurationPanel configurationPanel = new BiomartConfigurationPanel(getActivity().getConfiguration());
		final HelpEnabledDialog dialog = new HelpEnabledDialog((Frame) null, getRelativeName(), false, null);
		
		Action applyAction = new AbstractAction("Apply") {

			public void actionPerformed(ActionEvent arg0) {
				Element query = configurationPanel.getQuery();
				configureActivity(query);
			}
			
		};
		Action closeAction = new AbstractAction("Close") {

			public void actionPerformed(ActionEvent e) {
            	ActivityConfigurationAction.clearDialog(dialog);  
			}
			
		};

		configurationPanel.setOkAction(applyAction);
		configurationPanel.setCancelAction(closeAction);
		
		dialog.getContentPane().add(configurationPanel);
		dialog.pack();
		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				ActivityConfigurationAction.clearDialog(dialog);
			}
		});
		ActivityConfigurationAction.setDialog(getActivity(), dialog);
		
	} 

}
