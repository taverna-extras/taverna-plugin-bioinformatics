/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: MartQuery.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/12/13 11:38:55 $
 *               by   $Author: davidwithers $
 * Created on 08-May-2006
 *****************************************************************/
package org.biomart.martservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Dataset;
import org.biomart.martservice.query.Filter;
import org.biomart.martservice.query.Link;
import org.biomart.martservice.query.Query;
import org.ensembl.mart.lib.config.AttributeDescription;
import org.ensembl.mart.lib.config.DatasetConfig;

/**
 * Class for creating queries to send to a BioMart web service.
 * 
 * @author David Withers
 */
public class MartQuery {
	private MartService martService;

	private MartDataset martDataset;

	private Query query;

	private Map<String, String> linkedDatasets = new HashMap<String, String>();

	private String softwareVersion;

	public MartQuery() {

	}

	public MartQuery(MartService martService, MartDataset martDataset, String requestId) {
		String version = null;
		try {
			version = martService.getVersion(martDataset.getMartURLLocation());
		} catch (MartServiceException e) {
			//assume it's a pre 0.5 mart service
		}
		setMartService(martService);
		setMartDataset(martDataset);
		if (version == null || version.equals("0.4")) {
			setQuery(new Query(martDataset.getVirtualSchema(), null, requestId));
		} else {
			setQuery(new Query(martDataset.getVirtualSchema(), version, requestId));
		}
	}

	/**
	 * @param martService
	 * @param martDataset
	 * @param query
	 */
	public MartQuery(MartService martService, MartDataset martDataset,
			Query query) {
		setMartService(martService);
		setMartDataset(martDataset);
		setQuery(query);
	}

	/**
	 * Returns the martDataset.
	 * 
	 * @return the martDataset.
	 */
	public MartDataset getMartDataset() {
		return martDataset;
	}

	/**
	 * Sets the martDataset.
	 * 
	 * @param martDataset
	 *            the martDataset to set.
	 */
	public void setMartDataset(MartDataset martDataset) {
		this.martDataset = martDataset;
	}

	/**
	 * Returns the martService.
	 * 
	 * @return the martService.
	 */
	public MartService getMartService() {
		return martService;
	}

	/**
	 * Sets the martService.
	 * 
	 * @param martService
	 *            the martService to set.
	 */
	public void setMartService(MartService martService) {
		this.martService = martService;
	}

	/**
	 * Returns the query.
	 * 
	 * @return the query.
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * Sets the query.
	 * 
	 * @param query
	 *            the query to set.
	 */
	public void setQuery(Query query) {
		this.query = query;
		softwareVersion = query.getSoftwareVersion();
	}

	/**
	 * Adds the ID that links the specified dataset to the initial dataset.
	 * 
	 * @param datasetName
	 *            the dataset
	 * @param linkId
	 *            the link ID
	 */
	public void addLinkedDataset(String datasetName, String linkId) {
		linkedDatasets.put(datasetName, linkId);
	}

	/**
	 * Removes a dataset and any datasets linked to it.
	 * 
	 * @param datasetName
	 *            the dataset to remove
	 */
	public void removeLinkedDataset(String datasetName) {
		linkedDatasets.remove(datasetName);
		if (query.containsDataset(datasetName)) {
			Dataset dataset = query.getDataset(datasetName);
			dataset.removeAllAttributes();
			dataset.removeAllFilters();
			query.removeDataset(dataset);
			for (Link link : query.getLinks(datasetName)) {
				removeLinkedDataset(link.getTarget());
			}

		}
		if (query.containsLink(datasetName)) {
			query.removeLink(query.getLink(datasetName));
		}
	}

	/**
	 * Changes the ID that links a dataset. This method performs no function,
	 * nor does it throw an exception, if the specified dataset does not exist.
	 * 
	 * @param datasetName
	 *            the dataset
	 * @param linkId
	 *            the link ID
	 */
	public void changeLinkedDataset(String datasetName, String linkId) {
		if (linkedDatasets.containsKey(datasetName)) {
			linkedDatasets.put(datasetName, linkId);
			if (query.containsLink(datasetName)) {
				query.getLink(datasetName).setDefaultLink(linkId);
			}
		}
	}

	public Set<String> getLinkedDatasets() {
		return linkedDatasets.keySet();
	}

	public String getLink(String datasetName) {
		return (String) linkedDatasets.get(datasetName);
	}

	/**
	 * Returns the Datasets that this Query contains in the order specified by
	 * the links.
	 * 
	 * @return the Datasets that this Query contains in the order specified by
	 *         the links
	 */
	public List<Dataset> getDatasetsInLinkOrder() {
		if (query.getLinks().size() > 0) {
			List<Dataset> datasets = new ArrayList<Dataset>();
			datasets.addAll(getLinkedDatasets(martDataset.getName()));
			// add other datasets
			for (Dataset dataset : query.getDatasets()) {
				if (!datasets.contains(dataset)) {
					datasets.add(dataset);
				}
			}
			return datasets;
		} else {
			return query.getDatasets();
		}
	}

	/**
	 * @param dataset
	 * @return
	 */
	private List<Dataset> getLinkedDatasets(String dataset) {
		List<Dataset> datasets = new ArrayList<Dataset>();
		datasets.add(query.getDataset(dataset));
		if (query.containsLink(dataset)) {
			Link link = query.getLink(dataset);
			if (!link.getTarget().equals(martDataset.getName())) {
				datasets.addAll(getLinkedDatasets(link.getTarget()));
			}
		}
		return datasets;
	}

	/**
	 * Returns all the Attributes from all the Datasets in this Query in the
	 * order specified by the links.
	 * 
	 * @return all the Attributes from all the Datasets in this Query in the
	 *         order specified by the links
	 */
	public List<Attribute> getAttributesInLinkOrder() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Dataset> datasets;
		if (softwareVersion == null) {
			datasets = getDatasetsInLinkOrder();
		} else {
			datasets = query.getDatasets();
		}
		for (Dataset dataset : datasets) {
			attributes.addAll(dataset.getAttributes());
		}
		return attributes;
	}

	/**
	 * Adds an Attribute to the Dataset with the given datasetName. If the Query
	 * has no Dataset with the given datasetName then a new Dataset is created.
	 * 
	 * @param datasetName
	 *            the name of the Dataset to add the Attribute to
	 * @param attribute
	 *            the Attribute to add
	 * @throws MartServiceException
	 */
	public void addAttribute(String datasetName, Attribute attribute) {
		if (!query.containsDataset(datasetName)) {
			Dataset dataset = new Dataset(datasetName);
			if (datasetName.equals(martDataset.getName())) {
				query.addDataset(0, dataset);
			} else {
				query.addDataset(dataset);
			}
		}
		Dataset dataset = query.getDataset(datasetName);
		dataset.addAttribute(attribute);
	}

	/**
	 * Removes an Attribute from its containing Dataset.
	 * 
	 * @param attribute
	 */
	public void removeAttribute(Attribute attribute) {
		Dataset dataset = attribute.getContainingDataset();
		if (dataset != null) {
			if (dataset.removeAttribute(attribute)) {
				if (!dataset.hasAttributes() && !dataset.hasFilters()) {
					Query query = dataset.getContainingQuery();
					if (query != null) {
						query.removeDataset(dataset);
					}
				}
			}
		}
	}

	/**
	 * Adds a Filter to the Dataset with the given datasetName. If the Query has
	 * no Dataset with the given datasetName then a new Dataset is created.
	 * 
	 * @param datasetName
	 *            the name of the Dataset to add the Filter to
	 * @param filter
	 *            the Filter to add
	 * @throws MartServiceException
	 */
	public void addFilter(String datasetName, Filter filter) {
		if (!query.containsDataset(datasetName)) {
			Dataset dataset = new Dataset(datasetName);
			if (datasetName.equals(martDataset.getName())) {
				query.addDataset(0, dataset);
			} else {
				query.addDataset(dataset);
			}
		}
		Dataset dataset = query.getDataset(datasetName);
		dataset.addFilter(filter);
	}

	/**
	 * Removes a Filter from its containing Dataset.
	 * 
	 * @param filter
	 */
	public void removeFilter(Filter filter) {
		Dataset dataset = filter.getContainingDataset();
		if (dataset != null) {
			if (dataset.removeFilter(filter)) {
				if (!dataset.hasAttributes() && !dataset.hasFilters()) {
					Query query = dataset.getContainingQuery();
					if (query != null) {
						query.removeDataset(dataset);
					}
				}
			}
		}
	}

	/**
	 * @throws MartServiceException
	 */
	public void calculateLinks() throws MartServiceException {
		if (softwareVersion == null) {
			if (!martService.linksCalculated()) {
				martService.calculateLinks();
			}
			for (Link link : query.getLinks()) {
				query.removeLink(link);
			}
			for (Dataset dataset : query.getDatasets()) {
				if (!martDataset.getName().equals(dataset.getName())) {
					addLinks(dataset.getName());
				}
			}
		}
	}

	/**
	 * @param source
	 * @throws MartServiceException
	 */
	public void addLinks(String source) throws MartServiceException {
		MartDataset sourceDataset = martService.getDataset(martDataset
				.getVirtualSchema(), source);
		MartDataset targetDataset = martDataset;
		List<MartDataset> path = martService.getPath(sourceDataset, targetDataset);
		if (path == null) {
			path = martService.getPath(targetDataset, sourceDataset);
		}
		if (path == null) {
			throw new MartServiceException("No link between " + source
					+ " and " + targetDataset.getName());
		}
		Iterator<MartDataset> iter = path.iterator();
		if (iter.hasNext()) {
			MartDataset lastDataset = (MartDataset) iter.next();
			while (iter.hasNext()) {
				MartDataset dataset = (MartDataset) iter.next();
				DatasetLink link = martService.getLinkBetween(lastDataset,
						dataset);
				String linkId = link.getLinks()[0];
				if (link.getLinks().length > 1) {
					if (getLink(source) != null) {
						linkId = getLink(source);
					} else {
						List<Attribute> attributes = query.getDataset(source)
								.getAttributes();
						if (attributes.size() > 0) {
							Attribute attribute = (Attribute) attributes.get(0);
							DatasetConfig config = martService
									.getDatasetConfig(sourceDataset);
							if (config.containsAttributeDescription(attribute
									.getName())) {
								AttributeDescription description = config
										.getAttributeDescriptionByInternalName(attribute
												.getName());
								String datasetLink = description
										.getDatasetLink();
								if (datasetLink != null) {
									linkId = datasetLink;
								}
							}
						}
					}
				}
				query.addLink(new Link(lastDataset.getName(),
						dataset.getName(), linkId));
				lastDataset = dataset;
			}
		}
	}

}
