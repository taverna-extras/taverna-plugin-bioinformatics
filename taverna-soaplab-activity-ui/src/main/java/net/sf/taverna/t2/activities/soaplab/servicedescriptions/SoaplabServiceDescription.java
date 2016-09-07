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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import org.apache.taverna.scufl2.api.configurations.Configuration;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class SoaplabServiceDescription extends ServiceDescription {

	public static final URI ACTIVITY_TYPE = URI.create("http://ns.taverna.org.uk/2010/activity/soaplab");

	private final static String SOAPLAB = "Soaplab @ ";

	private String category;
	private String operation;
	private URI endpoint;
	private List<String> types;

	private String name;

	public List<String> getTypes() {
		return types;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(final String operation) {
		this.operation = operation;

		String name = operation;
		int finalColon = operation.lastIndexOf(":");
		if (finalColon != -1) {
			name = operation.substring(finalColon + 1);
		}
		int finalDot = operation.lastIndexOf(".");
		if (finalDot != -1) {
			name = operation.substring(finalDot + 1);
		}
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public URI getActivityType() {
		return ACTIVITY_TYPE;
	}

	@Override
	public Configuration getActivityConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setType(ACTIVITY_TYPE.resolve("#Config"));
		((ObjectNode) configuration.getJson()).put("endpoint", getEndpoint().toASCIIString() + getOperation());
		((ObjectNode) configuration.getJson()).put("pollingInterval", 0);
		((ObjectNode) configuration.getJson()).put("pollingBackoff", 1.0);
		((ObjectNode) configuration.getJson()).put("pollingIntervalMax", 0);
		return configuration;
	}

	@Override
	public Icon getIcon() {
		return SoaplabActivityIcon.getSoaplabIcon();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getPath() {
		List<String> path = new ArrayList<String>();
		path.add(SOAPLAB + getEndpoint());
		path.add(getCategory());
		// Don't use getTypes() - as we end up
		// with double entries..
		return path;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

	public URI getEndpoint() {
		return endpoint;
	}

	@Override
	protected List<Object> getIdentifyingData() {
		return Arrays.<Object>asList(getEndpoint(), getOperation());
	}

}
