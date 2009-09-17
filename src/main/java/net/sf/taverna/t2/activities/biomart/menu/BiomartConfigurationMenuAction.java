package net.sf.taverna.t2.activities.biomart.menu;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomart.BiomartActivity;
import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class BiomartConfigurationMenuAction extends
AbstractConfigureActivityMenuAction<BiomartActivity>{

	public BiomartConfigurationMenuAction() {
		super(BiomartActivity.class);
	}

	@Override
	protected Action createAction() {
		BiomartActivityConfigurationAction configAction = new BiomartActivityConfigurationAction(findActivity(), getParentFrame());
		configAction.putValue(Action.NAME, BiomartActivityConfigurationAction.CONFIGURE_BIOMART);
		addMenuDots(configAction);
		return configAction;
	}

}
