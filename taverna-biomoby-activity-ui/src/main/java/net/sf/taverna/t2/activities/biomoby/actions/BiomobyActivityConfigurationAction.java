package net.sf.taverna.t2.activities.biomoby.actions;
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

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.view.BiomobyConfigView;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class BiomobyActivityConfigurationAction extends
		ActivityConfigurationAction<BiomobyActivity, BiomobyActivityConfigurationBean> {

	private final Frame owner;
	private static Logger logger = Logger.getLogger(BiomobyActivityConfigurationAction.class);
	private final EditManager editManager;
	private final FileManager fileManager;

	public BiomobyActivityConfigurationAction(BiomobyActivity activity, Frame owner,
			EditManager editManager, FileManager fileManager,
			ActivityIconManager activityIconManager) {
		super(activity, activityIconManager);
		this.owner = owner;
		this.editManager = editManager;
		this.fileManager = fileManager;
	}

	public void actionPerformed(ActionEvent arg0) {
		ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}

		final BiomobyConfigView biomobyConfigView = new BiomobyConfigView(
				(BiomobyActivity) getActivity());
		final ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean> dialog = new ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean>(
				getActivity(), biomobyConfigView, editManager, fileManager);

		ActivityConfigurationAction.setDialog(getActivity(), dialog, fileManager);
	}

	public boolean isEnabled() {
		BiomobyActivity activity = (BiomobyActivity) getActivity();
		return (activity.getMobyService() != null && activity.containsSecondaries());
	}

}
