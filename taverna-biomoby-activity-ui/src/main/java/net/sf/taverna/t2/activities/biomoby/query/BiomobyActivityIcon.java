package net.sf.taverna.t2.activities.biomoby.query;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 *
 * @author Alex Nenadic
 *
 */
public class BiomobyActivityIcon implements ActivityIconSPI{

	public int canProvideIconScore(Activity<?> activity) {
		if (activity.getClass().getName().equals(BiomobyActivity.class.getName()))
			return DEFAULT_ICON + 1;
		else
			return NO_ICON;
	}

	public Icon getIcon(Activity<?> activity) {
		return getBiomobyIcon();
	}

	public static Icon getBiomobyIcon() {
		return new ImageIcon(BiomobyActivityIcon.class.getResource("/registry.gif"));
	}

}
