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

import javax.swing.Action;

import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.jdom.Element;

import uk.org.taverna.configuration.app.ApplicationConfiguration;
import uk.org.taverna.scufl2.api.activity.Activity;

public class BiomartActivityConfigurationAction extends ActivityConfigurationAction {

	private static final long serialVersionUID = 3782223454010961660L;
	private final Frame owner;
	public static final String CONFIGURE_BIOMART = "Configure Biomart query";
	private final EditManager editManager;
	private final FileManager fileManager;
	private final ApplicationConfiguration applicationConfiguration;

	public BiomartActivityConfigurationAction(Activity activity, Frame owner,
			EditManager editManager, FileManager fileManager, ActivityIconManager activityIconManager,
			ApplicationConfiguration applicationConfiguration, ServiceDescriptionRegistry serviceDescriptionRegistry) {
		super(activity, activityIconManager, serviceDescriptionRegistry);
		this.editManager = editManager;
		this.fileManager = fileManager;
		this.applicationConfiguration = applicationConfiguration;
		putValue(Action.NAME, CONFIGURE_BIOMART);
		this.owner = owner;
	}

	@SuppressWarnings("serial")
	public void actionPerformed(ActionEvent action) {
		ActivityConfigurationDialog currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}

		final BiomartConfigurationPanel configurationPanel = new BiomartConfigurationPanel(
				getActivity(), applicationConfiguration, getServiceDescription());
		final ActivityConfigurationDialog dialog = new ActivityConfigurationDialog(
				getActivity(), configurationPanel, editManager, fileManager);

		ActivityConfigurationAction.setDialog(getActivity(), dialog, fileManager);
	}

}
