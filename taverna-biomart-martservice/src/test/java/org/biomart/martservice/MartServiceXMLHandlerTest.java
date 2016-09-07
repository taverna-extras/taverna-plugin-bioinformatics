/*
package org.biomart.martservice;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * 
 * @author David Withers
 */
public class MartServiceXMLHandlerTest extends TestCase {
	private XMLOutputter xmlOutputter;

	private SAXBuilder saxBuilder;

	private Namespace namespace;

	private MartService martService;

	private String martServiceXML;

	private MartURLLocation martUrlLocation;

	private String martUrlLocationXML;

	private MartDataset martDataset;

	private String martDatasetXML;

	private MartRegistry martRegistry;

	private String martRegistryXML;

	private String martRegistryXML2;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		xmlOutputter = new XMLOutputter();
		saxBuilder = new SAXBuilder();
		namespace = Namespace.getNamespace("test-namespace");
		martService = MartService.getMartService("url-location");
		martServiceXML = "<MartService xmlns=\"test-namespace\" location=\"url-location\" />";
		martUrlLocation = new MartURLLocation();
		martUrlLocation.setDefault(true);
		martUrlLocation.setDisplayName("location-display-name");
		martUrlLocation.setHost("host");
		martUrlLocation.setName("location-name");
		martUrlLocation.setPort(42);
		martUrlLocation.setServerVirtualSchema("server-virtual-schema");
		martUrlLocation.setVirtualSchema("virtual-schema");
		martUrlLocation.setVisible(false);
		martUrlLocation.setRedirect(true);
		martUrlLocationXML = "<MartURLLocation xmlns=\"test-namespace\" default=\"1\" displayName=\"location-display-name\" host=\"host\" name=\"location-name\" port=\"42\" serverVirtualSchema=\"server-virtual-schema\" virtualSchema=\"virtual-schema\" visible=\"0\" redirect=\"1\" />";
		martDataset = new MartDataset();
		martDataset.setDisplayName("dataset-display-name");
		martDataset.setInitialBatchSize(1);
		martDataset.setMartURLLocation(martUrlLocation);
		martDataset.setMaximumBatchSize(2);
		martDataset.setName("dataset-name");
		martDataset.setType("type");
		martDataset.setVisible(true);
		martDataset.setInterface("interface");
		martDataset.setModified("modified");
		martDatasetXML = "<MartDataset xmlns=\"test-namespace\" displayName=\"dataset-display-name\" name=\"dataset-name\" type=\"type\" initialBatchSize=\"1\" maximumBatchSize=\"2\" visible=\"true\" interface=\"interface\" modified=\"modified\"><MartURLLocation default=\"1\" displayName=\"location-display-name\" host=\"host\" name=\"location-name\" port=\"42\" serverVirtualSchema=\"server-virtual-schema\" virtualSchema=\"virtual-schema\" visible=\"0\" redirect=\"1\" /></MartDataset>";
		martRegistry = new MartRegistry();
		martRegistry.addMartURLLocation(martUrlLocation);
		martRegistryXML = "<MartRegistry xmlns=\"test-namespace\" ><virtualSchema xmlns=\"test-namespace\" name=\"virtual-schema\" visible=\"0\"><MartURLLocation xmlns=\"test-namespace\" default=\"1\" displayName=\"location-display-name\" host=\"host\" name=\"location-name\" port=\"42\" serverVirtualSchema=\"server-virtual-schema\" visible=\"0\" redirect=\"1\" /></virtualSchema></MartRegistry>";
		martRegistryXML2 = "<MartRegistry xmlns=\"test-namespace\" ><virtualSchema xmlns=\"test-namespace\" name=\"default\" visible=\"0\"><MartURLLocation xmlns=\"test-namespace\" default=\"1\" displayName=\"location-display-name\" host=\"host\" name=\"location-name\" port=\"42\" serverVirtualSchema=\"server-virtual-schema\" visible=\"0\" redirect=\"1\" /></virtualSchema></MartRegistry>";
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.martServiceToElement(MartService,
	 * Namespace)'
	 */
	public void testMartServiceToElement() {
		Element element = MartServiceXMLHandler.martServiceToElement(
				martService, namespace);
		assertEquals(xmlOutputter.outputString(element), martServiceXML);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.elementToMartService(Element)'
	 */
	public void testElementToMartService() throws JDOMException, IOException {
		Element element = saxBuilder.build(new StringReader(martServiceXML))
				.getRootElement();
		assertEquals(MartServiceXMLHandler.elementToMartService(element),
				martService);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.datasetToElement(MartDataset,
	 * Namespace)'
	 */
	public void testDatasetToElement() {
		Element element = MartServiceXMLHandler.datasetToElement(martDataset,
				namespace);
		assertEquals(xmlOutputter.outputString(element), martDatasetXML);
//		System.out.println(new XMLOutputter().outputString(element));
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.elementToDataset(Element,
	 * Namespace)'
	 */
	public void testElementToDataset() throws JDOMException, IOException {
		Element element = saxBuilder.build(new StringReader(martDatasetXML))
				.getRootElement();
		assertEquals(
				MartServiceXMLHandler.elementToDataset(element, namespace),
				martDataset);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.elementToRegistry(Element,
	 * Namespace)'
	 */
	public void testElementToRegistry() throws JDOMException, IOException {
		Element element = saxBuilder.build(new StringReader(martRegistryXML))
				.getRootElement();
		assertEquals(MartServiceXMLHandler
				.elementToRegistry(element, namespace), martRegistry);

		martUrlLocation.setVirtualSchema("default");
		element = saxBuilder.build(new StringReader(martRegistryXML2))
		.getRootElement();
		assertEquals(MartServiceXMLHandler
				.elementToRegistry(element, namespace), martRegistry);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.locationToElement(MartURLLocation,
	 * Namespace)'
	 */
	public void testLocationToElement() {
		Element element = MartServiceXMLHandler.locationToElement(
				martUrlLocation, namespace);
		assertEquals(xmlOutputter.outputString(element), martUrlLocationXML);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.elementToLocation(Element)'
	 */
	public void testElementToLocation() throws JDOMException, IOException {
		Element element = saxBuilder
				.build(new StringReader(martUrlLocationXML)).getRootElement();
		assertEquals(MartServiceXMLHandler.elementToLocation(element),
				martUrlLocation);
	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.elementToMartQuery(Element,
	 * Namespace)'
	 */
	public void testElementToMartQuery() {

	}

	/*
	 * Test method for
	 * 'org.biomart.martservice.MartServiceXMLHandler.martQueryToElement(MartQuery,
	 * Namespace)'
	 */
	public void testMartQueryToElement() {

	}

}
