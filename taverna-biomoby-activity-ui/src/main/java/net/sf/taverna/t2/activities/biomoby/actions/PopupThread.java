package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Component;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;


public class PopupThread extends Thread {

	Object object = null;

	BiomobyObjectActivity objectActivity = null;

	BiomobyObjectActionHelper objectAction = null;

	boolean done = false;

	PopupThread(BiomobyObjectActivity bop, BiomobyObjectActionHelper boa) {
		super("Biomoby popup");
		this.objectAction = boa;
		this.objectActivity = bop;
		setDaemon(true);
	}

	public void run() {
		object = objectAction.getComponent(objectActivity);
		this.done = true;
	}

	// call after you check if done!
	public Component getComponent() {
		return (Component) object;
	}
}
