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

import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Utility class for serializing <code>Query</code> classes to XML.
 *
 * @author David Withers
 */
public class QueryXMLHandler {
	public static final String QUERY_ELEMENT = "Query";

	public static final String DATASET_ELEMENT = "Dataset";

	public static final String ATTRIBUTE_ELEMENT = "Attribute";

	public static final String FILTER_ELEMENT = "Filter";

	public static final String LINK_ELEMENT = "Links";

	public static final String ATTRIBUTES_ATTRIBUTE = "attributes";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String COUNT_ATTRIBUTE = "count";

	public static final String UNIQUE_ROWS_ATTRIBUTE = "uniqueRows";

	public static final String VERSION_ATTRIBUTE = "softwareVersion";

	public static final String FORMATTER_ATTRIBUTE = "formatter";

	public static final String HEADER_ATTRIBUTE = "header";

	public static final String REQUEST_ID_ATTRIBUTE = "requestId";

	public static final String SCHEMA_ATTRIBUTE = "virtualSchemaName";

	/**
	 * Converts a <code>Query</code> to an XML element.
	 *
	 * @param query
	 *            the <code>Query</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>Query</code>
	 */
	public static Element queryToElement(Query query, Namespace namespace) {
		Element queryElement = new Element(QUERY_ELEMENT, namespace);
		String virtualSchemaName = query.getVirtualSchemaName();
		if (virtualSchemaName == null) {
			queryElement.setAttribute(SCHEMA_ATTRIBUTE, "default");
		} else {
			queryElement.setAttribute(SCHEMA_ATTRIBUTE, virtualSchemaName);
		}
		queryElement.setAttribute(COUNT_ATTRIBUTE, String.valueOf(query.getCount()));
		queryElement.setAttribute(UNIQUE_ROWS_ATTRIBUTE, String.valueOf(query.getUniqueRows()));
		String softwareVersion = query.getSoftwareVersion();
		if (softwareVersion != null) {
			queryElement.setAttribute(VERSION_ATTRIBUTE, softwareVersion);
		}
		String formatter = query.getFormatter();
		if (formatter != null) {
			queryElement.setAttribute(FORMATTER_ATTRIBUTE, formatter);
			queryElement.setAttribute(HEADER_ATTRIBUTE, "1");
		}
		String requestId = query.getRequestId();
		if (requestId != null) {
			queryElement.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
		}
		for (Dataset dataset : query.getDatasets()) {
			queryElement.addContent(datasetToElement(dataset, namespace));
		}
		for (Link link : query.getLinks()) {
			queryElement.addContent(linkToElement(link, namespace));
		}

		return queryElement;
	}

	/**
	 * Converts a <code>Dataset</code> to an XML element.
	 *
	 * @param dataset
	 *            the <code>Dataset</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>Dataset</code>
	 */
	public static Element datasetToElement(Dataset dataset, Namespace namespace) {
		Element datasetElement = new Element(DATASET_ELEMENT, namespace);
		datasetElement.setAttribute(NAME_ATTRIBUTE, dataset.getName());

		for (Attribute attribute : dataset.getAttributes()) {
			datasetElement.addContent(attributeToElement(attribute, namespace));
		}

		for (Filter filter : dataset.getFilters()) {
			datasetElement.addContent(filterToElement(filter, namespace));
		}

		return datasetElement;
	}

	/**
	 * Converts a <code>Link</code> to an XML element.
	 *
	 * @param link
	 *            the <code>Link</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>Link</code>
	 */
	public static Element linkToElement(Link link, Namespace namespace) {
		Element linkElement = new Element(LINK_ELEMENT, namespace);
		linkElement.setAttribute("source", link.getSource());
		linkElement.setAttribute("target", link.getTarget());
		linkElement.setAttribute("defaultLink", link.getDefaultLink());
		return linkElement;
	}

	/**
	 * Converts an <code>Attribute</code> to an XML element.
	 *
	 * @param attribute
	 *            the <code>Attribute</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>Attribute</code>
	 */
	public static Element attributeToElement(Attribute attribute,
			Namespace namespace) {
		Element attributeElement = new Element(ATTRIBUTE_ELEMENT, namespace);
		attributeElement.setAttribute(NAME_ATTRIBUTE, attribute.getName());
		String attributes = attribute.getAttributes();
		if (attributes != null) {
			attributeElement.setAttribute(ATTRIBUTES_ATTRIBUTE, attributes);
		}
		return attributeElement;
	}

	/**
	 * Converts a <code>Filter</code> to an XML element.
	 *
	 * @param filter
	 *            the <code>Filter</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>Filter</code>
	 */
	public static Element filterToElement(Filter filter, Namespace namespace) {
		Element filterElement = new Element(FILTER_ELEMENT, namespace);
		filterElement.setAttribute(NAME_ATTRIBUTE, filter.getName());
		String value = filter.getValue();
		if (filter.isBoolean()) {
			if ("excluded".equalsIgnoreCase(value)) {
				filterElement.setAttribute("excluded", "1");
			} else {
				filterElement.setAttribute("excluded", "0");
			}
		} else {
			if (value == null) {
				filterElement.setAttribute("value", "");
			} else {
				filterElement.setAttribute("value", value);
			}
		}
		if (filter.isList()) {
			filterElement.setAttribute("list", "true");
		}
		return filterElement;
	}

	/**
	 * Creates a <code>Query</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @param namespace
	 *            the <code>Namespace</code> containing the
	 *            <code>Element</code>
	 * @return a deserialized <code>Query</code>
	 */
	public static Query elementToQuery(Element element, Namespace namespace) {
		String virtualSchema = element.getAttributeValue(SCHEMA_ATTRIBUTE);
		int count = Integer.parseInt(element.getAttributeValue(COUNT_ATTRIBUTE));
		String version = element.getAttributeValue(VERSION_ATTRIBUTE);
		String formatter = element.getAttributeValue(FORMATTER_ATTRIBUTE);
		String requestId = element.getAttributeValue(REQUEST_ID_ATTRIBUTE);
		Query query = new Query(virtualSchema, count, version, requestId);
		query.setFormatter(formatter);
		String uniqueRows = element.getAttributeValue(UNIQUE_ROWS_ATTRIBUTE);
		if (uniqueRows != null) {
			query.setUniqueRows(Integer.parseInt(uniqueRows));
		}
		List<Element> datasets = element.getChildren(DATASET_ELEMENT, namespace);
		for (Element datasetElement : datasets) {
			query.addDataset(elementToDataset(datasetElement, namespace));
		}
		List<Element> links = element.getChildren(LINK_ELEMENT, namespace);
		for (Element linkElement : links) {
			query.addLink(elementToLink(linkElement));
		}
		return query;
	}

	/**
	 * Creates a <code>Dataset</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @param namespace
	 *            the <code>Namespace</code> containing the
	 *            <code>Element</code>
	 * @return a deserialized <code>Dataset</code>
	 */
	public static Dataset elementToDataset(Element element, Namespace namespace) {
		Dataset dataset = new Dataset(element.getAttributeValue(NAME_ATTRIBUTE));

		List<Element> attributes = element.getChildren(ATTRIBUTE_ELEMENT, namespace);
		for (Element attributeElement : attributes) {
			dataset.addAttribute(elementToAttribute(attributeElement));
		}

		List<Element> filters = element.getChildren(FILTER_ELEMENT, namespace);
		for (Element filterElement : filters) {
			dataset.addFilter(elementToFilter(filterElement));
		}
		return dataset;
	}

	/**
	 * Creates a <code>Filter</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @return a deserialized <code>Filter</code>
	 */
	public static Filter elementToFilter(Element element) {
		Filter filter;
		String filterName = element.getAttributeValue(NAME_ATTRIBUTE);
		String filterValue = element.getAttributeValue("value");
		if (filterValue != null) {
			filter = new Filter(filterName, filterValue);
		} else {
			filterValue = element.getAttributeValue("excluded");
			if ("1".equals(filterValue)) {
				filter = new Filter(filterName, "excluded", true);
			} else {
				filter = new Filter(filterName, "only", true);
			}
		}
		String listValue = element.getAttributeValue("list");
		if ("true".equals(listValue)) {
			filter.setList(true);
		}
		return filter;
	}

	/**
	 * Creates an <code>Attribute</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @return a deserialized <code>Attribute</code>
	 */
	public static Attribute elementToAttribute(Element element) {
		String attributeName = element.getAttributeValue(NAME_ATTRIBUTE);
		Attribute attribute = new Attribute(attributeName);
		String attributes = element.getAttributeValue(ATTRIBUTES_ATTRIBUTE);
		if (attributes != null) {
			attribute.setAttributes(attributes);
		}
		return attribute;
	}

	/**
	 * Creates an <code>Link</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @return a deserialized <code>Link</code>
	 * @deprecated MartJ 0.5 won't require links to be specified
	 */
	public static Link elementToLink(Element element) {
		return new Link(element.getAttributeValue("source"), element
				.getAttributeValue("target"), element
				.getAttributeValue("defaultLink"));
	}

}
