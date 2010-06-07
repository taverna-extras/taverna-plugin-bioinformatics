/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;

/**
 * @author Stuart Owen
 * 
 */
@SuppressWarnings("serial")
public class MobyServiceDetailsAction extends AbstractAction {

	private final BiomobyActivity activity;
	private final Frame owner;

	private static final String MOBY_SERVICE_DETAILS_ACTION = "Browse Biomoby service details";
	public MobyServiceDetailsAction(BiomobyActivity activity, Frame owner) {
		this.activity = activity;
		this.owner = owner;
		putValue(NAME, MOBY_SERVICE_DETAILS_ACTION);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		BiomobyActionHelper helper = new BiomobyActionHelper();
		Dimension size = helper.getFrameSize();
		
		Component component = helper.getComponent(activity);
		final JDialog dialog = new HelpEnabledDialog(owner, helper.getDescription(), false, null);

		dialog.getContentPane().add(component);
		dialog.pack();
//		dialog.setTitle(helper.getDescription());
		dialog.setSize(size);
//		dialog.setModal(false);
		dialog.setVisible(true);

	}

}
