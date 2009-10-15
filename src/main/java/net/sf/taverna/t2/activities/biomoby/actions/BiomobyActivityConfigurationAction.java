/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JDialog;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.view.BiomobyConfigView;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;

import org.apache.log4j.Logger;
import org.biomoby.service.dashboard.data.ParametersTable;

@SuppressWarnings("serial")
public class BiomobyActivityConfigurationAction
		extends
		ActivityConfigurationAction<BiomobyActivity, BiomobyActivityConfigurationBean> {

	private final Frame owner;
	private static Logger logger = Logger
			.getLogger(BiomobyActivityConfigurationAction.class);

	public BiomobyActivityConfigurationAction(BiomobyActivity activity,
			Frame owner) {
		super(activity);
		this.owner = owner;
	}

	public void actionPerformed(ActionEvent arg0) {
		ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean> currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		
		final BiomobyConfigView biomobyConfigView = new BiomobyConfigView((BiomobyActivity)getActivity());
		final ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean> dialog =
			new ActivityConfigurationDialog<BiomobyActivity, BiomobyActivityConfigurationBean>(getActivity(), biomobyConfigView);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);	
	}
	
	public boolean isEnabled() {
		BiomobyActivity activity = (BiomobyActivity)getActivity();
		return (activity.getMobyService() != null && activity.containsSecondaries());
	}

}
