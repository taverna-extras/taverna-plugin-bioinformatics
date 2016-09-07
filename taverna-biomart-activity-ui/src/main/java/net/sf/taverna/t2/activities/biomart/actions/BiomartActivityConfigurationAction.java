package net.sf.taverna.t2.activities.biomart.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;
import uk.org.taverna.commons.services.ServiceRegistry;
import uk.org.taverna.configuration.app.ApplicationConfiguration;
import org.apache.taverna.scufl2.api.activity.Activity;

@SuppressWarnings("serial")
public class BiomartActivityConfigurationAction extends ActivityConfigurationAction {

	public static final String CONFIGURE_BIOMART = "Configure Biomart query";
	private final EditManager editManager;
	private final FileManager fileManager;
	private final ApplicationConfiguration applicationConfiguration;
	private final ServiceRegistry serviceRegistry;

	public BiomartActivityConfigurationAction(Activity activity, Frame owner,
			EditManager editManager, FileManager fileManager, ActivityIconManager activityIconManager,
			ApplicationConfiguration applicationConfiguration, ServiceDescriptionRegistry serviceDescriptionRegistry,
			ServiceRegistry serviceRegistry) {
		super(activity, activityIconManager, serviceDescriptionRegistry);
		this.editManager = editManager;
		this.fileManager = fileManager;
		this.applicationConfiguration = applicationConfiguration;
		this.serviceRegistry = serviceRegistry;
		putValue(Action.NAME, CONFIGURE_BIOMART);
	}

	public void actionPerformed(ActionEvent action) {
		ActivityConfigurationDialog currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}

		final BiomartConfigurationPanel configurationPanel = new BiomartConfigurationPanel(
				getActivity(), applicationConfiguration, serviceRegistry);
		final ActivityConfigurationDialog dialog = new ActivityConfigurationDialog(
				getActivity(), configurationPanel, editManager);

		ActivityConfigurationAction.setDialog(getActivity(), dialog, fileManager);
	}

}
