package org.biomart.martservice.query;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biomart.martservice.MartServiceException;

/**
 * Class for creating mart queries.
 *
 * @author David Withers
 */
public class Query {
	private String virtualSchemaName;

	private int count;

	private int uniqueRows;

	private String softwareVersion;

	private String formatter;

	private String requestId;

	private List<Dataset> datasets = new ArrayList<Dataset>();

	private Map<String, Dataset> datasetMap = new HashMap<String, Dataset>();

	private Set<Link> links = new HashSet<Link>();

	private Map<String, Link> linkSourceMap = new HashMap<String, Link>();

	private List<QueryListener> listeners = new ArrayList<QueryListener>();

	/**
	 * Constructs an instance of a <code>Query</code> with the specified
	 * <code>virtualSchemaName</code> and a <code>count</code> of 0.
	 *
	 * @param virtualSchemaName
	 */
	public Query(String virtualSchemaName) {
		this(virtualSchemaName, 0);
	}

	/**
	 * Constructs an instance of a <code>Query</code> with the specified
	 * <code>virtualSchemaName</code> and <code>count</code>.
	 *
	 * @param virtualSchemaName
	 * @param count
	 */
	public Query(String virtualSchemaName, int count) {
		setVirtualSchemaName(virtualSchemaName);
		setCount(count);
	}

	/**
	 * Constructs an instance of a <code>Query</code> with the specified
	 * <code>virtualSchemaName</code>, <code>softwareVersion</code> and
	 * <code>requestId</code>.
	 *
	 * @param virtualSchemaName
	 * @param softwareVersion
	 * @param requestId
	 */
	public Query(String virtualSchemaName, String softwareVersion,
			String requestId) {
		this(virtualSchemaName, 0, softwareVersion, requestId);
	}

	/**
	 * Constructs an instance of a <code>Query</code> with the specified
	 * <code>virtualSchemaName</code>, <code>count</code> and
	 * <code>softwareVersion</code>.
	 *
	 * @param virtualSchemaName
	 * @param count
	 * @param softwareVersion
	 */
	public Query(String virtualSchemaName, int count, String softwareVersion) {
		this(virtualSchemaName, count, softwareVersion, null);
	}

	/**
	 * Constructs an instance of a <code>Query</code> with the specified
	 * <code>virtualSchemaName</code>, <code>count</code>,
	 * <code>softwareVersion</code> and <code>requestId</code>.
	 *
	 * @param virtualSchemaName
	 * @param count
	 * @param softwareVersion
	 * @param requestId
	 */
	public Query(String virtualSchemaName, int count, String softwareVersion,
			String requestId) {
		setVirtualSchemaName(virtualSchemaName);
		setCount(count);
		setSoftwareVersion(softwareVersion);
		setRequestId(requestId);
	}

	/**
	 * Constructs an instance of a <code>Query</code> which is a deep copy of
	 * another <code>Query</code>.
	 *
	 * @param query
	 *            the <code>Query</code> to copy
	 * @throws MartServiceException
	 */
	public Query(Query query) {
		setVirtualSchemaName(query.virtualSchemaName);
		setCount(query.count);
		setUniqueRows(query.uniqueRows);
		setSoftwareVersion(query.softwareVersion);
		setFormatter(query.formatter);
		setRequestId(query.requestId);
		for (Dataset dataset : query.getDatasets()) {
			addDataset(new Dataset(dataset));
		}
		for (Link link : query.getLinks()) {
			addLink(new Link(link));
		}
	}

	/**
	 * Returns the virtualSchema.
	 *
	 * @return the virtualSchema.
	 */
	public String getVirtualSchemaName() {
		return virtualSchemaName;
	}

	/**
	 * Sets the virtualSchema.
	 *
	 * @param virtualSchemaName
	 *            the virtualSchema to set.
	 */
	public void setVirtualSchemaName(String virtualSchemaName) {
		this.virtualSchemaName = virtualSchemaName;
	}

	/**
	 * Returns the count.
	 *
	 * @return the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count
	 *            the count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Returns the uniqueRows query attribute
	 *
	 * @return the uniqueRows query attribute
	 */
	public int getUniqueRows() {
		return uniqueRows;
	}

	/**
	 * Sets the uniqueRows query attribute.
	 *
	 * Valid values are 0 or 1.
	 *
	 * @param uniqueRows value for the uniqueRows query attribute
	 */
	public void setUniqueRows(int uniqueRows) {
		this.uniqueRows = uniqueRows;
	}

	/**
	 * Returns the softwareVersion.
	 *
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * Sets the softwareVersion.
	 *
	 * @param softwareVersion
	 *            the new softwareVersion
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * Returns the formatter.
	 *
	 * @return the formatter
	 */
	public String getFormatter() {
		return formatter;
	}

	/**
	 * Sets the formatter.
	 *
	 * @param formatter the new formatter
	 */
	public void setFormatter(String formatter) {
		if (this.formatter == null) {
			if (formatter != null) {
				this.formatter = formatter;
				fireFormatterAdded(formatter);
			}
		} else if (!this.formatter.equals(formatter)) {
			if (formatter == null) {
				String removedFormatter = this.formatter;
				this.formatter = formatter;
				fireFormatterRemoved(removedFormatter);
			} else {
				this.formatter = formatter;
				fireFormatterChanged(formatter);
			}
		}
	}

	/**
	 * Returns the requestId.
	 *
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Sets the requestId.
	 *
	 * @param requestId
	 *            the new requestId
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Adds a Dataset to the Query.
	 *
	 * The Dataset is added at the end of the list of Datasets.
	 *
	 * @param dataset
	 *            the Dataset to add
	 */
	public void addDataset(Dataset dataset) {
		addDataset(datasets.size(), dataset);
	}

	/**
	 * Adds a Dataset to the Query at the specified position.
	 *
	 * @param index
	 *            the position to add the Dataset
	 * @param dataset
	 *            the Dataset to add
	 */
	public void addDataset(int index, Dataset dataset) {
		datasets.add(index, dataset);
		datasetMap.put(dataset.getName(), dataset);
		if (dataset.getContainingQuery() != null) {
			dataset.getContainingQuery().removeDataset(dataset);
		}
		dataset.setContainingQuery(this);
	}

	/**
	 * Removes a Dataset from the Query.
	 *
	 * @param dataset
	 *            the Dataset to remove
	 */
	public void removeDataset(Dataset dataset) {
		datasets.remove(dataset);
		datasetMap.remove(dataset.getName());
		dataset.setContainingQuery(null);
	}

	/**
	 * Removes all the Datasets from the Query.
	 */
	public void removeAllDatasets() {
		for (Dataset dataset : datasets) {
			dataset.setContainingQuery(null);
		}
		datasets.clear();
		datasetMap.clear();
	}

	/**
	 * Returns the Datasets that this Query contains.
	 *
	 * @return the Datasets that this Query contains.
	 */
	public List<Dataset> getDatasets() {
		return new ArrayList<Dataset>(datasets);
	}

	/**
	 * Returns a Dataset with the given datasetName. If the Query has no Dataset
	 * with the given datasetName null is returned.
	 *
	 * @param datasetName
	 * @return a Dataset with the given datasetName
	 */
	public Dataset getDataset(String datasetName) {
		return (Dataset) datasetMap.get(datasetName);
	}

	/**
	 * Returns true if this Query contains a Dataset with the name
	 * <code>datasetName</code>.
	 *
	 * @param datasetName
	 * @return true if this Query contains a Dataset with the name
	 *         <code>datasetName</code>.
	 */
	public boolean containsDataset(String datasetName) {
		return datasetMap.containsKey(datasetName);
	}

	/**
	 * Adds a Link to the Query.
	 *
	 * @param link
	 *            the Link to add
	 */
	public void addLink(Link link) {
		links.add(link);
		linkSourceMap.put(link.getSource(), link);
		if (link.getContainingQuery() != null) {
			link.getContainingQuery().removeLink(link);
		}
		link.setContainingQuery(this);
	}

	/**
	 * Removes a link from the Query
	 *
	 * @param link
	 *            the Link to remove
	 */
	public void removeLink(Link link) {
		links.remove(link);
		linkSourceMap.remove(link.getSource());
		link.setContainingQuery(null);
	}

	/**
	 * Returns the Links that this Query contains.
	 *
	 * @return the Links that this Query contains.
	 */
	public Set<Link> getLinks() {
		return new HashSet<Link>(links);
	}

	/**
	 * Returns a Link with the given source. If the Query has no Link with the
	 * given source null is returned.
	 *
	 * @param source
	 *            the source of the link
	 * @return a Link with the given source
	 */
	public Link getLink(String source) {
		return (Link) linkSourceMap.get(source);
	}

	/**
	 * Returns true if this Query contains a Link with the same source.
	 *
	 * @param source
	 *            the source of the link
	 * @return true if this Query contains a Link with the same source.
	 */
	public boolean containsLink(String source) {
		return linkSourceMap.containsKey(source);
	}

	/**
	 * Returns a List of Links with the given target. If the Query has no Link
	 * with the given target an empty List is returned.
	 *
	 * @param target
	 *            the target of the link
	 * @return a Link with the given target
	 */
	public Set<Link> getLinks(String target) {
		Set<Link> result = new HashSet<Link>();
		Set<Link> links = getLinks();
		for (Link link: links) {
			if (link.getTarget().equals(target)) {
				result.add(link);
			}
		}
		return result;
	}

	/**
	 * Returns all the Attributes from all the Datasets in this Query.
	 *
	 * @return all the Attributes from all the Datasets in this Query.
	 */
	public List<Attribute> getAttributes() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (Dataset dataset : datasets) {
			attributes.addAll(dataset.getAttributes());
		}
		return attributes;
	}

	/**
	 * Returns all the Filters from all the Datasets in this Query.
	 *
	 * @return all the Filters from all the Datasets in this Query.
	 */
	public List<Filter> getFilters() {
		List<Filter> filters = new ArrayList<Filter>();
		for (Dataset dataset : datasets) {
			filters.addAll(dataset.getFilters());
		}
		return filters;
	}

	/**
	 * Adds the specified query listener to receive query events. If
	 * <code>listener</code> is null, no exception is thrown and no action is
	 * performed.
	 *
	 * @param listener
	 *            the query listener
	 */
	public void addQueryListener(QueryListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes the specified query listener so that it no longer receives query
	 * events. This method performs no function, nor does it throw an exception,
	 * if <code>listener</code> was not previously added to this component. If
	 * <code>listener</code> is null, no exception is thrown and no action is
	 * performed.
	 *
	 * @param listener
	 *            the query listener
	 */
	public void removeQueryListener(QueryListener listener) {
		listeners.remove(listener);
	}

	void fireAttributeAdded(Attribute attribute, Dataset dataset) {
		for (QueryListener listener : listeners) {
			listener.attributeAdded(attribute, dataset);
		}
	}

	void fireAttributeRemoved(Attribute attribute, Dataset dataset) {
		for (QueryListener listener : listeners) {
			listener.attributeRemoved(attribute, dataset);
		}
	}

	void fireFilterAdded(Filter filter, Dataset dataset) {
		for (QueryListener listener : listeners) {
			listener.filterAdded(filter, dataset);
		}
	}

	void fireFilterRemoved(Filter filter, Dataset dataset) {
		for (QueryListener listener : listeners) {
			listener.filterRemoved(filter, dataset);
		}
	}

	void fireFilterChanged(Filter filter, Dataset dataset) {
		for (QueryListener listener : listeners) {
			listener.filterChanged(filter, dataset);
		}
	}

	void fireFormatterAdded(String formatter) {
		for (QueryListener listener : listeners) {
			listener.formatterAdded(formatter);
		}
	}

	void fireFormatterRemoved(String formatter) {
		for (QueryListener listener : listeners) {
			listener.formatterRemoved(formatter);
		}
	}

	void fireFormatterChanged(String formatter) {
		for (QueryListener listener : listeners) {
			listener.formatterChanged(formatter);
		}
	}

}
