package net.sf.taverna.t2.activities.biomart.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomart.BiomartActivity;
import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;

public class BiomartConfigurationMenuAction extends
AbstractConfigureActivityMenuAction<BiomartActivity>{

	private EditManager editManager;
	private FileManager fileManager;

	public BiomartConfigurationMenuAction() {
		super(BiomartActivity.class);
	}

	@Override
	protected Action createAction() {
		BiomartActivityConfigurationAction configAction = new BiomartActivityConfigurationAction(findActivity(), getParentFrame(), editManager, fileManager);
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

}
