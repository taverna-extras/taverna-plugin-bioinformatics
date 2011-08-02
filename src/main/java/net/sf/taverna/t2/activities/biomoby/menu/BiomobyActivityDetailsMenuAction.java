package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.MobyServiceDetailsAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workflowmodel.Edits;

public class BiomobyActivityDetailsMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private static final String CONFIGURE_BIOMOBY_DETAILS = "Browse Biomoby service details";
	private EditManager editManager;
	private FileManager fileManager;

	public BiomobyActivityDetailsMenuAction() {
		super(BiomobyActivity.class);
	}

	@Override
	protected Action createAction() {
		MobyServiceDetailsAction configAction = new MobyServiceDetailsAction(
				findActivity(), getParentFrame(), editManager, fileManager);
		configAction.putValue(Action.NAME, CONFIGURE_BIOMOBY_DETAILS);
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
