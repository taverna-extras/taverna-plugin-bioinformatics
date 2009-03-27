/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.util.HashMap;
import java.util.Map;

/**
 * A configuration bean specific to the Biomoby activity.
 * 
 * @author David Withers
 */
public class BiomobyActivityConfigurationBean {

	private String mobyEndpoint="";

	private String serviceName="";

	private String authorityName="";
	
	private String category = "";
	
	private String serviceType = "";
	
	private Map<String,String> secondaries=new HashMap<String,String>();

	/**
	 * Returns the mobyEndpoint.
	 *
	 * @return the mobyEndpoint
	 */
	public String getMobyEndpoint() {
		return mobyEndpoint;
	}

	/**
	 * Sets the mobyEndpoint.
	 *
	 * @param mobyEndpoint the new mobyEndpoint
	 */
	public void setMobyEndpoint(String mobyEndpoint) {
		this.mobyEndpoint = mobyEndpoint;
	}

	/**
	 * Returns the serviceName.
	 *
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the serviceName.
	 *
	 * @param serviceName the new serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Returns the authorityName.
	 *
	 * @return the authorityName
	 */
	public String getAuthorityName() {
		return authorityName;
	}

	/**
	 * Sets the authorityName.
	 *
	 * @param authorityName the new authorityName
	 */
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
	
	/**
	 * Returns the secondaries
	 * 
	 * @return secondaries as a HashMap
	 */
	public Map<String,String> getSecondaries() {
		return secondaries;
	}
	
	/**
	 * 
	 * @param secondaries
	 */
	public void setSecondaries(Map<String,String> secondaries) {
		this.secondaries=secondaries;
	}

	/**
	 * Returns the category of moby service
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category
	 * 
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Returns the service type for this service
	 * @return the service type
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Sets the service type
	 * @param serviceType the service type to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	

}
