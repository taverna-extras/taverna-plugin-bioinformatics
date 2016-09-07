package net.sf.taverna.t2.activities.biomart.servicedescriptions;

import java.net.URI;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;

/**
 *
 * @author Alex Nenadic
 * @author Alan R Williams
 *
 */
public class BiomartActivityIcon implements ActivityIconSPI {

	private static final URI BIOMART_ACTIVITY = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");

	static Icon icon = null;

	public int canProvideIconScore(URI activityType) {
		if (BIOMART_ACTIVITY.equals(activityType))
			return DEFAULT_ICON + 1;
		else
			return NO_ICON;
	}

	public Icon getIcon(URI activityType) {
		return getBiomartIcon();
	}

	public static Icon getBiomartIcon() {
		if (icon == null) {
			icon = new ImageIcon(BiomartActivityIcon.class.getResource("/biomart.png"));
		}
		return icon;
	}
}
