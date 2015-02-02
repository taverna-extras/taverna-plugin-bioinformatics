package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.MobyParserAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;

public class BiomobyActivityParserMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private static final String ADD_BIOMOBY_PARSER = "Add Biomoby parser";

	private EditManager editManager;

	private FileManager fileManager;

	public BiomobyActivityParserMenuAction() {
		super(BiomobyActivity.class);
	}

	@Override
	protected Action createAction() {
		MobyParserAction configAction = new MobyParserAction(
				findActivity(), getParentFrame(), editManager, fileManager);
		configAction.putValue(Action.NAME, ADD_BIOMOBY_PARSER);
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
