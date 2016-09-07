package org.biomart.martservice;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.biomart.martservice.query.QueryXMLHandler;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Utility class for serializing mart service classes to XML.
 *
 * @author David Withers
 */
public class MartServiceXMLHandler {
	public static final String MART_SERVICE_ELEMENT = "MartService";

	public static final String MART_URL_LOCATION_ELEMENT = "MartURLLocation";

	public static final String MART_DATASET_ELEMENT = "MartDataset";

	public static final String MART_QUERY_ELEMENT = "MartQuery";

	public static final String LINKED_DATASETS_ELEMENT = "LinkedDatasets";

	public static final String LINKED_DATASET_ELEMENT = "LinkedDataset";

	public static final String VIRTUAL_SCHEMA_ELEMENT = "virtualSchema";

	public static final String LOCATION_ATTRIBUTE = "location";

	public static final String DATABASE_ATTRIBUTE = "database";

	public static final String DEFAULT_ATTRIBUTE = "default";

	public static final String DEFAULT_VALUE_ATTRIBUTE = "defaultValue";

	public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";

	public static final String HOST_ATTRIBUTE = "host";

	public static final String INCLUDE_DATASETS_ATTRIBUTE = "includeDatasets";

	public static final String LINK_ATTRIBUTE = "LINK";

	public static final String MART_USER_ATTRIBUTE = "martUser";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String PATH_ATTRIBUTE = "path";

	public static final String PORT_ATTRIBUTE = "port";

	public static final String TYPE_ATTRIBUTE = "type";

	public static final String INITIAL_BATCH_SIZE_ATTRIBUTE = "initialBatchSize";

	public static final String MAXIMUM_BATCH_SIZE_ATTRIBUTE = "maximumBatchSize";

	public static final String VIRTUAL_SCHEMA_ATTRIBUTE = "virtualSchema";

	public static final String SERVER_VIRTUAL_SCHEMA_ATTRIBUTE = "serverVirtualSchema";

	public static final String VISIBLE_ATTRIBUTE = "visible";

	public static final String REDIRECT_ATTRIBUTE = "redirect";

	public static final String INTERFACE_ATTRIBUTE = "interface";

	public static final String MODIFIED_ATTRIBUTE = "modified";

	/**
	 * Converts a <code>MartService</code> to an XML element.
	 *
	 * @param martService
	 *            the <code>MartService</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>MartService</code>
	 */
	public static Element martServiceToElement(MartService martService,
			Namespace namespace) {
		Element element = new Element(MART_SERVICE_ELEMENT, namespace);
		element.setAttribute(LOCATION_ATTRIBUTE, martService.getLocation());
		return element;
	}

	/**
	 * Creates a <code>MartService</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @return a deserialized <code>MartService</code>
	 */
	public static MartService elementToMartService(Element element) {
		return MartService.getMartService(element
				.getAttributeValue(LOCATION_ATTRIBUTE));
	}

	/**
	 * Converts a <code>MartDataset</code> to an XML element.
	 *
	 * @param dataset
	 *            the <code>MartDataset</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>MartDataset</code>
	 */
	public static Element datasetToElement(MartDataset dataset,
			Namespace namespace) {
		Element element = new Element(MART_DATASET_ELEMENT, namespace);
		element.setAttribute(DISPLAY_NAME_ATTRIBUTE, dataset.getDisplayName());
		element.setAttribute(NAME_ATTRIBUTE, dataset.getName());
		element.setAttribute(TYPE_ATTRIBUTE, dataset.getType());
		element.setAttribute(INITIAL_BATCH_SIZE_ATTRIBUTE, String
				.valueOf(dataset.getInitialBatchSize()));
		element.setAttribute(MAXIMUM_BATCH_SIZE_ATTRIBUTE, String
				.valueOf(dataset.getMaximumBatchSize()));
		element.setAttribute(VISIBLE_ATTRIBUTE, String.valueOf(dataset
				.isVisible()));
		if (dataset.getInterface() != null) {
			element.setAttribute(INTERFACE_ATTRIBUTE, dataset.getInterface());
		}
		if (dataset.getModified() != null) {
			element.setAttribute(MODIFIED_ATTRIBUTE, dataset.getModified());
		}
		element.addContent(locationToElement(dataset.getMartURLLocation(),
				namespace));
		return element;
	}

	/**
	 * Creates a <code>MartDataset</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @param namespace
	 *            the <code>Namespace</code> containing the
	 *            <code>Element</code>
	 * @return a deserialized <code>MartDataset</code>
	 */
	public static MartDataset elementToDataset(Element element,
			Namespace namespace) {
		MartDataset dataset = new MartDataset();
		dataset.setDisplayName(element
				.getAttributeValue(DISPLAY_NAME_ATTRIBUTE));
		dataset.setName(element.getAttributeValue(NAME_ATTRIBUTE));
		dataset.setType(element.getAttributeValue(TYPE_ATTRIBUTE));
		dataset.setInitialBatchSize(Long.parseLong(element
				.getAttributeValue(INITIAL_BATCH_SIZE_ATTRIBUTE)));
		dataset.setMaximumBatchSize(Long.parseLong(element
				.getAttributeValue(MAXIMUM_BATCH_SIZE_ATTRIBUTE)));
		dataset.setVisible(Boolean.getBoolean(element
				.getAttributeValue(VISIBLE_ATTRIBUTE)));
		dataset.setInterface(element.getAttributeValue(INTERFACE_ATTRIBUTE));
		dataset.setModified(element.getAttributeValue(MODIFIED_ATTRIBUTE));
		dataset.setMartURLLocation(elementToLocation(element.getChild(
				MART_URL_LOCATION_ELEMENT, namespace)));
		return dataset;
	}

	/**
	 * Creates a <code>MartRegistry</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @param namespace
	 *            the <code>Namespace</code> containing the
	 *            <code>Element</code>
	 * @return a deserialized <code>MartRegistry</code>
	 */
	public static MartRegistry elementToRegistry(Element root,
			Namespace namespace) {
		MartRegistry registry = new MartRegistry();
		List<Element> children = root.getChildren();
		for (Element childElement : children) {
			if (childElement.getNamespace().equals(namespace)) {
				if (childElement.getName().equals(MART_URL_LOCATION_ELEMENT)) {
					MartURLLocation martURLLocation = MartServiceXMLHandler
							.elementToLocation(childElement);
					martURLLocation.setVirtualSchema("default");
					registry.addMartURLLocation(martURLLocation);
				} else if (childElement.getName()
						.equals(VIRTUAL_SCHEMA_ELEMENT)) {
					String virtualSchema = childElement
							.getAttributeValue(NAME_ATTRIBUTE);
					List<Element> locations = childElement.getChildren(
							MART_URL_LOCATION_ELEMENT, namespace);
					for (Element location : locations) {
						MartURLLocation martURLLocation = MartServiceXMLHandler
								.elementToLocation(location);
						martURLLocation.setVirtualSchema(virtualSchema);
						registry.addMartURLLocation(martURLLocation);
					}
				}
			}
		}
		return registry;
	}

	/**
	 * Converts a <code>MartURLLocation</code> to an XML element.
	 *
	 * @param location
	 *            the <code>MartURLLocation</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>MartURLLocation</code>
	 */
	public static Element locationToElement(MartURLLocation location,
			Namespace namespace) {
		Element element = new Element(MART_URL_LOCATION_ELEMENT, namespace);
		if (location.getDatabase() != null) {
			element.setAttribute(DATABASE_ATTRIBUTE, location.getDatabase());
		}
		element.setAttribute(DEFAULT_ATTRIBUTE, location.isDefault() ? "1"
				: "0");
		element.setAttribute(DISPLAY_NAME_ATTRIBUTE, location.getDisplayName());
		element.setAttribute(HOST_ATTRIBUTE, location.getHost());
		if (location.getIncludeDatasets() != null) {
			element.setAttribute(INCLUDE_DATASETS_ATTRIBUTE, location
					.getIncludeDatasets());
		}
		if (location.getMartUser() != null) {
			element.setAttribute(MART_USER_ATTRIBUTE, location.getMartUser());
		}
		element.setAttribute(NAME_ATTRIBUTE, location.getName());
		if (location.getPath() != null) {
			element.setAttribute(PATH_ATTRIBUTE, location.getPath());
		}
		element
				.setAttribute(PORT_ATTRIBUTE, String
						.valueOf(location.getPort()));
		element.setAttribute(SERVER_VIRTUAL_SCHEMA_ATTRIBUTE, location
				.getServerVirtualSchema());
		if (location.getVirtualSchema() != null) {
			element.setAttribute(VIRTUAL_SCHEMA_ATTRIBUTE, location
					.getVirtualSchema());
		}
		element.setAttribute(VISIBLE_ATTRIBUTE, location.isVisible() ? "1"
				: "0");
		element.setAttribute(REDIRECT_ATTRIBUTE, location.isRedirect() ? "1"
				: "0");
		return element;
	}

	/**
	 * Creates a <code>MartURLLocation</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @return a deserialized <code>MartURLLocation</code>
	 */
	public static MartURLLocation elementToLocation(Element element) {
		MartURLLocation location = new MartURLLocation();
		location.setDatabase(element.getAttributeValue(DATABASE_ATTRIBUTE));
		location.setDefault("1".equals(element
				.getAttributeValue(DEFAULT_ATTRIBUTE)));
		location.setDisplayName(element
				.getAttributeValue(DISPLAY_NAME_ATTRIBUTE));
		location.setHost(element.getAttributeValue(HOST_ATTRIBUTE));
		location.setIncludeDatasets(element
				.getAttributeValue(INCLUDE_DATASETS_ATTRIBUTE));
		location.setMartUser(element.getAttributeValue(MART_USER_ATTRIBUTE));
		location.setName(element.getAttributeValue(NAME_ATTRIBUTE));
		location.setPath(element.getAttributeValue(PATH_ATTRIBUTE));
		try {
			location.setPort(Integer.parseInt(element
					.getAttributeValue(PORT_ATTRIBUTE)));
		} catch (NumberFormatException e) {
			location.setPort(80);
		}
		location.setServerVirtualSchema(element
				.getAttributeValue(SERVER_VIRTUAL_SCHEMA_ATTRIBUTE));
		location.setVirtualSchema(element
				.getAttributeValue(VIRTUAL_SCHEMA_ATTRIBUTE));
		location.setVisible("1".equals(element
				.getAttributeValue(VISIBLE_ATTRIBUTE)));
		location.setRedirect("1".equals(element
				.getAttributeValue(REDIRECT_ATTRIBUTE)));
		return location;
	}

	/**
	 * Creates a <code>MartQuery</code> from an XML element.
	 *
	 * @param element
	 *            the <code>Element</code> to deserialize
	 * @param namespace
	 *            the <code>Namespace</code> containing the
	 *            <code>Element</code>
	 * @return a deserialized <code>MartQuery</code>
	 */
	public static MartQuery elementToMartQuery(Element element,
			Namespace namespace) {
		MartQuery martQuery = new MartQuery();
		martQuery.setMartService(MartServiceXMLHandler
				.elementToMartService(element.getChild(MART_SERVICE_ELEMENT,
						namespace)));
		martQuery.setMartDataset(elementToDataset(element.getChild(
				MART_DATASET_ELEMENT, namespace), namespace));
		martQuery.setQuery(QueryXMLHandler.elementToQuery(element.getChild(
				QueryXMLHandler.QUERY_ELEMENT, namespace), namespace));
		Element linksElement = element.getChild(LINKED_DATASETS_ELEMENT, namespace);
		if (linksElement != null) {
			List linkedDatasets = linksElement.getChildren(LINKED_DATASETS_ELEMENT,
					namespace);
			for (Iterator iter = linkedDatasets.iterator(); iter.hasNext();) {
				Element datasetElement = (Element) iter.next();
				String datasetName = datasetElement.getAttributeValue(NAME_ATTRIBUTE);
				String linkId = datasetElement.getAttributeValue(LINK_ATTRIBUTE);
				martQuery.addLinkedDataset(datasetName, linkId);
			}
		}
		return martQuery;
	}

	/**
	 * Converts a <code>MartQuery</code> to an XML element.
	 *
	 * @param martQuery
	 *            the <code>MartQuery</code> to serialize
	 * @param namespace
	 *            the <code>Namespace</code> to use when constructing the
	 *            <code>Element</code>
	 * @return an XML serialization of the <code>MartQuery</code>
	 */
	public static Element martQueryToElement(MartQuery martQuery,
			Namespace namespace) {
		Element element = new Element(MART_QUERY_ELEMENT, namespace);
		element.addContent(martServiceToElement(martQuery.getMartService(),
				namespace));
		element.addContent(datasetToElement(martQuery.getMartDataset(),
				namespace));
		element.addContent(QueryXMLHandler.queryToElement(martQuery.getQuery(),
				namespace));
		Set linkedDatasets = martQuery.getLinkedDatasets();
		if (linkedDatasets.size() > 0) {
			Element linksElement = new Element(LINKED_DATASETS_ELEMENT, namespace);
			for (Iterator iter = linkedDatasets.iterator(); iter.hasNext();) {
				String datasetName = (String) iter.next();
				Element datasetElement = new Element(LINKED_DATASETS_ELEMENT, namespace);
				datasetElement.setAttribute(NAME_ATTRIBUTE, datasetName);
				datasetElement.setAttribute(LINK_ATTRIBUTE, martQuery
						.getLink(datasetName));
				linksElement.addContent(datasetElement);
			}
			element.addContent(linksElement);
		}
		return element;
	}

}
