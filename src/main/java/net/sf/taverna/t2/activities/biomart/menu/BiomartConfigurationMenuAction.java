package net.sf.taverna.t2.activities.biomart.menu;

import java.net.URI;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.ui.menu.ContextualMenuComponent;
import net.sf.taverna.t2.ui.menu.MenuComponent;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import uk.org.taverna.commons.services.ServiceRegistry;
import uk.org.taverna.configuration.app.ApplicationConfiguration;

public class BiomartConfigurationMenuAction extends AbstractConfigureActivityMenuAction implements
		MenuComponent, ContextualMenuComponent {

	private static final URI BIOMART_ACTIVITY = URI
			.create("http://ns.taverna.org.uk/2010/activity/biomart");
	private EditManager editManager;
	private FileManager fileManager;
	private ActivityIconManager activityIconManager;
	private ApplicationConfiguration applicationConfiguration;
	private ServiceDescriptionRegistry serviceDescriptionRegistry;
	private ServiceRegistry serviceRegistry;

	public BiomartConfigurationMenuAction() {
		super(BIOMART_ACTIVITY);
	}

	@Override
	protected Action createAction() {
		BiomartActivityConfigurationAction configAction = new BiomartActivityConfigurationAction(
				findActivity(), getParentFrame(), editManager, fileManager, activityIconManager,
				applicationConfiguration, serviceDescriptionRegistry, serviceRegistry);
		configAction.putValue(Action.NAME, BiomartActivityConfigurationAction.CONFIGURE_BIOMART);
		addMenuDots(configAction);
		return configAction;
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setActivityIconManager(ActivityIconManager activityIconManager) {
		this.activityIconManager = activityIconManager;
	}

	public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}

	public void setServiceDescriptionRegistry(ServiceDescriptionRegistry serviceDescriptionRegistry) {
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
