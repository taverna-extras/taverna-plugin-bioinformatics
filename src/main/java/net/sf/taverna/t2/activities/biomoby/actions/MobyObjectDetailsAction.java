/**
 * Copyright (C) 2007 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 */
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class MobyObjectDetailsAction extends AbstractAction {
	
	private final BiomobyObjectActivity activity;
	private final Frame owner;

	private static final String MOBY_OBJECT_DETAILS_ACTION = "Datatype registry query";
	
	public MobyObjectDetailsAction(BiomobyObjectActivity activity, Frame owner) {
		this.activity = activity;
		this.owner = owner;
		putValue(NAME, MOBY_OBJECT_DETAILS_ACTION);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		BiomobyObjectActionHelper helper = new BiomobyObjectActionHelper();
		Dimension size = helper.getFrameSize();
		
		Component component = helper.getComponent(activity);
		final JDialog dialog = new JDialog(owner, false);

		dialog.getContentPane().add(component);
		dialog.pack();
		dialog.setTitle(helper.getDescription());
		dialog.setSize(size);
		dialog.setModal(false);
		dialog.setVisible(true);

	}

}
