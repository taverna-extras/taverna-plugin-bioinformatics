/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

/**
 * A configuration bean specific to the Moby Parse Datatype activity.
 * 
 * @author David Withers
 */
public class MobyParseDatatypeActivityConfigurationBean {

	private String datatypeName="";

	private String registryEndpoint="";
	
	private String articleNameUsedByService="";

	/**
	 * Returns the datatypeName.
	 *
	 * @return the datatypeName
	 */
	public String getDatatypeName() {
		return datatypeName;
	}

	/**
	 * Sets the datatypeName.
	 *
	 * @param datatypeName the new datatypeName
	 */
	public void setDatatypeName(String datatypeName) {
		this.datatypeName = datatypeName;
	}

	/**
	 * Returns the registryEndpoint.
	 *
	 * @return the registryEndpoint
	 */
	public String getRegistryEndpoint() {
		return registryEndpoint;
	}

	/**
	 * Sets the registryEndpoint.
	 *
	 * @param registryEndpoint the new registryEndpoint
	 */
	public void setRegistryEndpoint(String registryEndpoint) {
		this.registryEndpoint = registryEndpoint;
	}

	/**
	 * Returns the articleNameUsedByService.
	 *
	 * @return the articleNameUsedByService
	 */
	public String getArticleNameUsedByService() {
		return articleNameUsedByService;
	}

	/**
	 * Sets the articleNameUsedByService.
	 *
	 * @param articleNameUsedByService the new articleNameUsedByService
	 */
	public void setArticleNameUsedByService(String articleNameUsedByService) {
		this.articleNameUsedByService = articleNameUsedByService;
	}

}
