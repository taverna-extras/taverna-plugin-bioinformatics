package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;


/**
 * This class contains some methods that are useful in creating a consistent JPanel
 * for displaying information or actions for biomoby services and datatypes.
 *
 * @author Edward Kawas
 * @author Stuart Owen = initial port from T1 to T2
 *
 */
public class SimpleActionFrame extends JPanel {

		private static final long serialVersionUID = -6611234116434482238L;


		private String name = "";
		public SimpleActionFrame(Component c, String name) {
			super(new BorderLayout());
			add(c, BorderLayout.CENTER);

			this.name = name;
		}



		public String getName() {
			return name;
		}

		public void onDisplay() {

		}


	}
