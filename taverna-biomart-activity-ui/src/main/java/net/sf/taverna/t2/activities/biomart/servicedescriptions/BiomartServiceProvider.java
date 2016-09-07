/**
 *
 */
package net.sf.taverna.t2.activities.biomart.servicedescriptions;
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

import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;

import org.biomart.martservice.MartDataset;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartRegistry;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.MartURLLocation;

/**
 * @author alanrw
 * @author David Withers
 */
public class BiomartServiceProvider extends AbstractConfigurableServiceProvider<BiomartServiceProviderConfig> {

	private static final String TAVERNA = "taverna";
	private static final String BIOMART_SERVICE = "Biomart service";

	private static final URI providerId = URI
	.create("http://taverna.sf.net/2010/service-provider/biomart");

	private ServiceDescriptionRegistry serviceDescriptionRegistry;

	public BiomartServiceProvider() {
		super(new BiomartServiceProviderConfig("http://somehost/biomart"));
	}

	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		List<ServiceDescription> descriptions = new ArrayList<ServiceDescription>();
		String url = serviceProviderConfig.getUrl();
		callBack.status("About to parse biomart:" + url);
		try {
		MartService martService = MartService
				.getMartService(getBiomartServiceLocation(url));
		martService.setRequestId(TAVERNA);
		MartRegistry registry = martService.getRegistry();
		MartURLLocation[] martURLLocations = registry.getMartURLLocations();
		for (MartURLLocation martURLLocation : martURLLocations) {
			if (martURLLocation.isVisible()) {

				MartDataset[] datasets = martService
						.getDatasets(martURLLocation);
				Arrays.sort(datasets, MartDataset.getDisplayComparator());
				for (MartDataset dataset : datasets) {
					if (dataset.isVisible()) {
						BiomartServiceDescription item = new BiomartServiceDescription();
						item.setUrl(martService.getLocation());
						item.setLocation(martURLLocation.getDisplayName());
						item.setDataset(dataset.getName());
						item.setDescription(dataset.getDisplayName());
						MartQuery biomartQuery = new MartQuery(martService,
								dataset, TAVERNA);
						item.setMartQuery(MartServiceXMLHandler.martQueryToElement(biomartQuery, null));
						descriptions.add(item);
					}
				}
				if (descriptions.size() > 0) {
					callBack.partialResults(descriptions);
					descriptions.clear();
				}
			}
		}
		callBack.finished();
		}
		catch (MartServiceException e) {
			callBack.fail("Failed to load Biomart from " + url, e);
		}
	}

	public Icon getIcon() {
		return BiomartActivityIcon.getBiomartIcon();
	}

	public String getName() {
		return BIOMART_SERVICE;
	}

	/**
	 * Attempts to construct a valid MartService URL from the location given.
	 *
	 * @param biomartLocation
	 * @return a (hopefully) valid MartService URL
	 */
	private String getBiomartServiceLocation(String biomartLocation) {
		StringBuffer sb = new StringBuffer();
		if (biomartLocation.endsWith("martservice")) {
			sb.append(biomartLocation);
		} else if (biomartLocation.endsWith("martview")) {
			sb.append(biomartLocation.substring(0, biomartLocation
					.lastIndexOf("martview")));
			sb.append("martservice");
		} else if (biomartLocation.endsWith("/")) {
			sb.append(biomartLocation);
			sb.append("martservice");
		} else {
			sb.append(biomartLocation);
			sb.append("/martservice");
		}
		return sb.toString();
	}

	public List<BiomartServiceProviderConfig> getDefaultConfigurations() {

		List<BiomartServiceProviderConfig> defaults = new ArrayList<BiomartServiceProviderConfig>();

		// If defaults have failed to load from a configuration file then load them here.
		if (!serviceDescriptionRegistry.isDefaultSystemConfigurableProvidersLoaded()){
			defaults.add(new BiomartServiceProviderConfig(
					"http://www.biomart.org/biomart/martservice"));
		} // else return an empty list

		return defaults;

	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		List<String> result;
		result = Arrays.asList(getConfiguration().getUrl());
		return result;
	}

	public String getId() {
		return providerId.toString();
	}

	public void setServiceDescriptionRegistry(ServiceDescriptionRegistry serviceDescriptionRegistry) {
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

}
