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
import java.util.List;

/**
 * Class for creating dataset elements of mart queries.
 *
 * @author David Withers
 */
public class Dataset {
	private String name;

	private List<Attribute> attributes = new ArrayList<Attribute>();

	private List<Filter> filters = new ArrayList<Filter>();

	private Query containingQuery;

	/**
	 * Constructs an instance of a <code>Dataset</code> with the specified
	 * name.
	 *
	 * @param name
	 *            the name of the <code>Dataset</code>
	 */
	public Dataset(String name) {
		setName(name);
	}

	/**
	 * Constructs an instance of a <code>Dataset</code> which is a deep copy
	 * of another <code>Dataset</code>.
	 *
	 * @param dataset
	 *            the <code>Dataset</code> to copy
	 */
	public Dataset(Dataset dataset) {
		setName(dataset.getName());
		for (Attribute attribute : dataset.getAttributes()) {
			addAttribute(new Attribute(attribute));
		}
		for (Filter filter : dataset.getFilters()) {
			addFilter(new Filter(filter));
		}
	}

	/**
	 * Returns the name of the Dataset.
	 *
	 * @return the name of the Dataset.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the Dataset.
	 *
	 * @param name
	 *            the new name for this Dataset.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a List of the Attributes in this dataset.
	 *
	 * @return a List of the Attributes in this dataset
	 */
	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(attributes);
	}

	/**
	 * Adds an Attribute to the dataset. The attribute's containing dataset will
	 * be set to this dataset. If this dataset is in a query an attribute added
	 * event will be fired.
	 *
	 * @param attribute
	 *            the Attribute to add
	 * @return true if the Attribute is not already in the dataset
	 */
	public boolean addAttribute(Attribute attribute) {
		if (!attributes.contains(attribute)) {
			attributes.add(attribute);
			attribute.setContainingDataset(this);
			if (containingQuery != null) {
				containingQuery.fireAttributeAdded(attribute, this);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds an array of Attributes to the dataset.
	 *
	 * @param attributeArray
	 *            the array of Attributes to add
	 */
	public void addAttributes(Attribute[] attributeArray) {
		for (int i = 0; i < attributeArray.length; i++) {
			addAttribute(attributeArray[i]);
		}
	}

	/**
	 * Returns true if attribute is in the dataset.
	 *
	 * @param attribute
	 * @return true if attribute is in the dataset.
	 */
	public boolean hasAttribute(Attribute attribute) {
		return attributes.contains(attribute);
	}

	/**
	 * Returns true if the dataset contains any Attributes.
	 *
	 * @return true if the dataset contains any Attributes
	 */
	public boolean hasAttributes() {
		return attributes.size() > 0;
	}

	/**
	 * Removes an Attribute from the dataset.
	 *
	 * If the attribute is contained in this dataset:
	 * <ul>
	 * <li>The attribute's containing dataset will be set to null.
	 * <li>If this dataset is in a query an attribute removed event will be fired.
	 *
	 * @param attribute
	 *            the attribute to remove
	 * @return true if the attribute is removed
	 */
	public boolean removeAttribute(Attribute attribute) {
		if (attributes.remove(attribute)) {
			attribute.setContainingDataset(null);
			if (containingQuery != null) {
				containingQuery.fireAttributeRemoved(attribute, this);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes all the Attributes from this dataset.
	 */
	public void removeAllAttributes() {
		for (Attribute attribute : getAttributes()) {
			removeAttribute(attribute);
		}
	}

	/**
	 * Returns a List of the Filters in this dataset.
	 *
	 * @return a List of the Filters in this dataset
	 */
	public List<Filter> getFilters() {
		return new ArrayList<Filter>(filters);
	}

	/**
	 * Adds a Filter to the dataset. The filter's containing dataset will be set
	 * to this dataset. If this dataset is in a query a filter added event will
	 * be fired.
	 *
	 * @param filter
	 *            the Filter to add
	 * @return true if the Filter is not already in the dataset
	 */
	public boolean addFilter(Filter filter) {
		if (!filters.contains(filter)) {
			filters.add(filter);
			filter.setContainingDataset(this);
			if (containingQuery != null) {
				containingQuery.fireFilterAdded(filter, this);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds an array of Filters to the dataset.
	 *
	 * @param filterArray
	 *            the array of Filters to add
	 */
	public void addFilters(Filter[] filterArray) {
		for (int i = 0; i < filterArray.length; i++) {
			addFilter(filterArray[i]);
		}
	}

	/**
	 * Returns true if filter is in the dataset.
	 *
	 * @param filter
	 * @return true if filter is in the dataset
	 */
	public boolean hasFilter(Filter filter) {
		return filters.contains(filter);
	}

	/**
	 * Returns true if the dataset contains any filters.
	 *
	 * @return true if the dataset contains any filters
	 */
	public boolean hasFilters() {
		return filters.size() > 0;
	}

	/**
	 * Removes an Filter from the dataset.
	 *
	 * If the filter is contained in this dataset:
	 * <ul>
	 * <li>The filter's containing dataset will be set to null.
	 * <li>If this dataset is in a query an attribute removed event will be fired.
	 *
	 * @param filter
	 *            the filter to remove
	 * @return true if the filter is removed
	 */
	public boolean removeFilter(Filter filter) {
		if (filters.remove(filter)) {
			filter.setContainingDataset(null);
			if (containingQuery != null) {
				containingQuery.fireFilterRemoved(filter, this);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes all the filters from the dataset.
	 */
	public void removeAllFilters() {
		for (Filter filter : getFilters()) {
			removeFilter(filter);
		}
	}

	/**
	 * Returns the containingQuery.
	 *
	 * @return the containingQuery.
	 */
	public Query getContainingQuery() {
		return containingQuery;
	}

	/**
	 * Sets the containingQuery.
	 *
	 * @param containingQuery
	 *            the containingQuery to set.
	 */
	void setContainingQuery(Query containingQuery) {
		this.containingQuery = containingQuery;
	}

}
