package net.sf.taverna.t2.activities.biomoby.servicedescriptions;
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

import java.net.URI;

import net.sf.taverna.t2.lang.beans.PropertyAnnotated;

public class BiomobyServiceProviderConfig extends PropertyAnnotated {

	private URI endpoint;

	private URI namespace;

	public BiomobyServiceProviderConfig() {
	}

	public BiomobyServiceProviderConfig(String endpoint, String namespace) {
		this.endpoint = URI.create(endpoint.trim());
		this.namespace = URI.create(namespace.trim());
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public URI getNamespace() {
		return namespace;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

	public void setNamespace(URI namespace) {
		this.namespace = namespace;
	}

}
