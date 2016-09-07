package net.sf.taverna.t2.activities.biomoby.query;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 *
 * @author Alex Nenadic
 * @author Stuart Owen
 *
 */
public class BiomobyObjectActivityItem /*extends AbstractActivityItem*/ {

	String registryUrl;
	String serviceName;
	String authorityName;
	String registryUri;

	public String getRegistryUri() {
		return registryUri;
	}

	public void setRegistryUri(String registryUri) {
		this.registryUri = registryUri;
	}

	public String getRegistryUrl() {
		return registryUrl;
	}

	public void setRegistryUrl(String registryUrl) {
		this.registryUrl = registryUrl;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

//	@Override
//	public Object getConfigBean() {
//		BiomobyObjectActivityConfigurationBean bean = new BiomobyObjectActivityConfigurationBean();
//		bean.setAuthorityName(getAuthorityName());
//		bean.setServiceName(getServiceName());
//		bean.setMobyEndpoint(getRegistryUrl());
//		return bean;
//	}
//
//	@Override
//	public Icon getIcon() {
//		return new ImageIcon(BiomobyObjectActivityItem.class.getResource("/biomoby_object.png"));
//	}
//
//	@Override
//	public Activity<?> getUnconfiguredActivity() {
//		return new BiomobyObjectActivity();
//	}

	public String getType() {
		return "Biomoby Object";
	}

}
