package net.sf.taverna.t2.activities.biomoby.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.BiomobyActivityConfigurationAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class ConfigureBiomobyMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	public ConfigureBiomobyMenuAction() {
		super(BiomobyActivity.class);
	}
	
	@Override
	protected Action createAction() {
		BiomobyActivity a = findActivity();
		Action result = null;
		result = new BiomobyActivityConfigurationAction(
				a, getParentFrame());
		result.putValue(Action.NAME, "Configure");
		addMenuDots(result);
		return result;
	}

public boolean isEnabled() {
	BiomobyActivity activity = findActivity();
	return (super.isEnabled() && (activity.getMobyService() != null) && activity.containsSecondaries());
}
}
