package net.sf.taverna.t2.activities.soaplab.servicedescriptions;
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
import net.sf.taverna.t2.lang.beans.PropertyAnnotation;

public class SoaplabServiceProviderConfig extends PropertyAnnotated {

	private URI endpoint;
	
	public SoaplabServiceProviderConfig() {
	}

	public SoaplabServiceProviderConfig(String endpointURI) {
		this.setEndpoint(URI.create(endpointURI.trim()));
	}

	@PropertyAnnotation(displayName = "Soaplab location", preferred = true)
	public URI getEndpoint() {
		return endpoint;
	}

	public String toString() {
		return getEndpoint().toString();
	}

	public void setEndpoint(URI endpoint) {
		String uriString = endpoint.toString();
		if (!uriString.endsWith("/")) {
			uriString = uriString + "/";
			this.endpoint = URI.create(uriString);
		} else {
			this.endpoint = endpoint;
		}
	}
	
}
