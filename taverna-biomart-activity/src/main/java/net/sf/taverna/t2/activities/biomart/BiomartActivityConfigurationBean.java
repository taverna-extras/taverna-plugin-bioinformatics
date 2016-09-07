/*******************************************************************************
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart;

import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationBean;
import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationProperty;

import org.jdom.Element;

/**
 * Biomart configuration.
 *
 * @author David Withers
 */
@ConfigurationBean(uri = BiomartActivity.URI + "#Config")
public class BiomartActivityConfigurationBean {

	private Element martQuery;

	public Element getMartQuery() {
		return martQuery;
	}

	@ConfigurationProperty(name = "martQuery", label = "Mart Query", description = "Biomart query in XML")
	public void setMartQuery(Element martQuery) {
		this.martQuery = martQuery;
	}

}
