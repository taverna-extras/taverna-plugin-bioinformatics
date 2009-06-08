package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.MobyServiceDetailsAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class BiomobyActivityDetailsMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private static final String CONFIGURE_BIOMOBY_DETAILS = "View Biomoby details";

	public BiomobyActivityDetailsMenuAction() {
		super(BiomobyActivity.class);
	}
	
	@Override
	protected Action createAction() {
		MobyServiceDetailsAction configAction = new MobyServiceDetailsAction(
				findActivity(), getParentFrame());
		configAction.putValue(Action.NAME, CONFIGURE_BIOMOBY_DETAILS);
		addMenuDots(configAction);
		return configAction;
	}


}
