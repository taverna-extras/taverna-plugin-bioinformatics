/*******************************************************************************
 ******************************************************************************/
package net.sf.taverna.t2.activities.soaplab.servicedescriptions;

import java.net.URI;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;

/**
 *
 * @author Alex Nenadic
 *
 */
public class SoaplabActivityIcon implements ActivityIconSPI{

	private static Icon icon;

	public int canProvideIconScore(URI activityType) {
		if (SoaplabServiceDescription.ACTIVITY_TYPE.equals(activityType))
			return DEFAULT_ICON + 1;
		else
			return NO_ICON;
	}

	public Icon getIcon(URI activityType) {
		return getSoaplabIcon();
	}

	public static Icon getSoaplabIcon() {
		if (icon == null) {
			icon = new ImageIcon(SoaplabActivityIcon.class
					.getResource("/soaplab.png"));
		}
		return icon;
	}

}


