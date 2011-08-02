package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.BiomobyActivityConfigurationAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;

public class ConfigureBiomobyMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private EditManager editManager;
	private FileManager fileManager;

	public ConfigureBiomobyMenuAction() {
		super(BiomobyActivity.class);
	}

	@Override
	protected Action createAction() {
		BiomobyActivity a = findActivity();
		Action result = null;
		result = new BiomobyActivityConfigurationAction(a, getParentFrame(),
				editManager, fileManager);
		result.putValue(Action.NAME, "Configure");
		addMenuDots(result);
		return result;
	}

	public boolean isEnabled() {
		BiomobyActivity activity = findActivity();
		return (super.isEnabled() && (activity.getMobyService() != null) && activity
				.containsSecondaries());
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}
}
