/**
 * 
 */
package net.sf.taverna.t2.activities.biomart.servicedescriptions;

import net.sf.taverna.t2.lang.beans.PropertyAnnotated;
import net.sf.taverna.t2.lang.beans.PropertyAnnotation;

/**
 * @author alanrw
 *
 */
public class BiomartServiceProviderConfig  extends PropertyAnnotated {

	private String url;
	
	public BiomartServiceProviderConfig() {
	}

	public BiomartServiceProviderConfig(String url) {
		this.url = url.trim();
	}

	@PropertyAnnotation(displayName = "Biomart location", preferred = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.trim();
	}

	public String toString() {
		return getUrl();
	}
}
