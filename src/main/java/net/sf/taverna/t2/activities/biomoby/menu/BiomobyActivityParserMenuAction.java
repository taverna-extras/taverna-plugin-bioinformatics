package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.MobyParserAction;
import net.sf.taverna.t2.activities.biomoby.actions.MobyServiceDetailsAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class BiomobyActivityParserMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private static final String ADD_BIOMOBY_PARSER = "Add Biomoby parser";

	public BiomobyActivityParserMenuAction() {
		super(BiomobyActivity.class);
	}
	
	@Override
	protected Action createAction() {
		MobyParserAction configAction = new MobyParserAction(
				findActivity(), getParentFrame());
		configAction.putValue(Action.NAME, ADD_BIOMOBY_PARSER);
		addMenuDots(configAction);
		return configAction;
	}


}
