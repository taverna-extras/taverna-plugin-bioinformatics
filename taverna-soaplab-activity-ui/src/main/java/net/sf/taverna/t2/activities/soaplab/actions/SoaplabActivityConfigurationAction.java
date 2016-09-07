/*******************************************************************************
 ******************************************************************************/
package net.sf.taverna.t2.activities.soaplab.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;
import org.apache.taverna.scufl2.api.activity.Activity;

public class SoaplabActivityConfigurationAction extends ActivityConfigurationAction {

	private static final long serialVersionUID = 5076721332542691094L;
	private final Frame owner;
	public static final String CONFIGURE_SOAPLAB_ACTIVITY = "Configure Soaplab";
	private final EditManager editManager;
	private final FileManager fileManager;

	public SoaplabActivityConfigurationAction(Activity activity, Frame owner,
			EditManager editManager, FileManager fileManager,
			ActivityIconManager activityIconManager, ServiceDescriptionRegistry serviceDescriptionRegistry) {
		super(activity, activityIconManager, serviceDescriptionRegistry);
		this.editManager = editManager;
		this.fileManager = fileManager;
		putValue(Action.NAME, CONFIGURE_SOAPLAB_ACTIVITY);
		this.owner = owner;
	}

	public void actionPerformed(ActionEvent action) {
		ActivityConfigurationDialog currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}

		final SoaplabConfigurationPanel panel = new SoaplabConfigurationPanel(getActivity());
		final ActivityConfigurationDialog dialog = new ActivityConfigurationDialog(
				getActivity(), panel, editManager);

		ActivityConfigurationAction.setDialog(getActivity(), dialog, fileManager);
	}

}
