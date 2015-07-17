/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.sf.taverna.t2.activities.biomoby;

import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationBean;
import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationProperty;

/**
 * A configuration bean specific to the Moby Parse Datatype activity.
 *
 * @author David Withers
 */
@ConfigurationBean(uri = MobyParseDatatypeActivity.URI + "#Config")
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
	@ConfigurationProperty(name = "datatypeName", label = "Datatype Name", description = "")
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
	@ConfigurationProperty(name = "registryEndpoint", label = "Registry Endpoint", description = "")
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
	@ConfigurationProperty(name = "articleNameUsedByService", label = "Article Name Used By Service", description = "")
	public void setArticleNameUsedByService(String articleNameUsedByService) {
		this.articleNameUsedByService = articleNameUsedByService;
	}

}
