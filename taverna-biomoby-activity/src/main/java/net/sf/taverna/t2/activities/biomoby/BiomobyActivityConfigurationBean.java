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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationBean;
import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationProperty;

/**
 * A configuration bean specific to the Biomoby activity.
 *
 * @author David Withers
 */
@ConfigurationBean(uri = BiomobyActivity.URI + "#Config")
public class BiomobyActivityConfigurationBean {

	private String mobyEndpoint="";

	private String serviceName="";

	private String authorityName="";

	@Deprecated
	private String category = "";
	@Deprecated
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
	@ConfigurationProperty(name = "mobyEndpoint", label = "Moby Endpoint")
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
	@ConfigurationProperty(name = "serviceName", label = "Service Name")
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
	@ConfigurationProperty(name = "authorityName", label = "Authority Name")
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

	@ConfigurationProperty(name = "secondaries", label = "Secondaries", required=false)
	public void setSecondaries(Set<Secondary> secondaries) {
		Map<String,String> secondariesMap = new HashMap<String,String>();
		for (Secondary secondary : secondaries) {
			secondariesMap.put(secondary.getKey(), secondary.getValue());
		}
		this.secondaries=secondariesMap;
	}

	@ConfigurationBean(uri = BiomobyActivity.URI + "#Secondary")
	public static class Secondary {
		private String key, value;

		public String getKey() {
			return key;
		}

		@ConfigurationProperty(name = "key", label = "Key")
		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		@ConfigurationProperty(name = "value", label = "Value")
		public void setValue(String value) {
			this.value = value;
		}

	}

}
