/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.biomoby.shared.MobyException;
import org.biomoby.shared.Utils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 * @author Eddie Kawas
 * 
 */
@SuppressWarnings("unchecked")
public class XMLUtilities {

	// private variables
	// machine independent new line character
	public final static String newline = System.getProperty("line.separator");

	// class variable to keep persistant queryIDs
	private static int queryCount = 0;

	// the moby namespaces
	public final static Namespace MOBY_NS = Namespace.getNamespace("moby",
			"http://www.biomoby.org/moby");

	/**
	 * 
	 * @param message
	 *            the structurally valid BioMoby message as a String
	 * @return true if the message contains multiple invocations, false
	 *         otherwise.
	 * @throws MobyException
	 *             if the message is null
	 */
	public static boolean isMultipleInvocationMessage(String message)
			throws MobyException {
		if (message == null)
			throw new MobyException(
					newline
							+ "null 'xml' found where it was not expected in isMultipleInvocationMessage(String message).");
		Element documentElement = getDOMDocument(message).getRootElement();
		return isMultipleInvocationMessage(documentElement);
	}

	/**
	 * 
	 * @param message
	 *            the structurally valid BioMoby message as an Element
	 * @return true if the message contains multiple invocations, false
	 *         otherwise.
	 * @throws MobyException
	 *             if the message is null
	 */
	public static boolean isMultipleInvocationMessage(Element message)
			throws MobyException {
		if (message == null)
			throw new MobyException(
					newline
							+ "null 'xml' found where it was not expected in isMultipleInvocationMessage(Element message).");
		Element e = (Element) message.clone();
		List list = new ArrayList();
		listChildren(e, "mobyData", list);
		if (list != null)
			if (list.size() > 1)
				return true;
		return false;
	}

	/**
	 * 
	 * @param element
	 *            the element to extract the list of simples from. This method
	 *            assumes that you are passing in a single invokation and that
	 *            you wish to extract the Simples not contained in any
	 *            collections. This method also maintains any past queryIDs.
	 * @return an array of elements that are fully 'wrapped' simples.
	 * @throws MobyException
	 *             if the Element isnt structurally valid in terms of Moby
	 *             message structure
	 */
	public static Element[] getListOfSimples(Element element)
			throws MobyException {
		Element temp = (Element) element.clone();
		Element e = (Element) element.clone();
		String queryID = "";

		if (isMultipleInvocationMessage(element))
			return new Element[] {};

		Element serviceNotes = getServiceNotes(e);

		// if the current elements name isnt MOBY, see if its direct child is
		if (!e.getName().equals("MOBY")) {
			if (e.getChild("MOBY") != null)
				temp = e.getChild("MOBY");
			else if (e.getChild("MOBY", MOBY_NS) != null)
				temp = e.getChild("MOBY", MOBY_NS);
			else
				throw new MobyException(newline
						+ "Expected 'MOBY' as the local name for the element "
						+ newline + "and instead received '" + e.getName()
						+ "' (getListOfSimples(Element element).");
		}
		// parse the mobyContent node
		if (temp.getChild("mobyContent") != null)
			temp = temp.getChild("mobyContent");
		else if (temp.getChild("mobyContent", MOBY_NS) != null)
			temp = temp.getChild("mobyContent", MOBY_NS);
		else
			throw new MobyException(
					newline
							+ "Expected 'mobyContent' as the local name for the next child element but it "
							+ newline
							+ "wasn't there. I even tried a qualified name (getListOfSimples(Element element).");

		// parse the mobyData node
		if (temp.getChild("mobyData") != null) {
			temp = temp.getChild("mobyData");
		} else if (temp.getChild("mobyData", MOBY_NS) != null) {
			temp = temp.getChild("mobyData", MOBY_NS);
		} else {
			throw new MobyException(
					newline
							+ "Expected 'mobyData' as the local name for the next child element but it "
							+ newline
							+ "wasn't there. I even tried a qualified name (getListOfSimples(Element element).");
		}

		// temp == mobyData now we need to get the queryID and save it
		if (temp.getAttribute("queryID") != null) {
			queryID = temp.getAttribute("queryID").getValue();
		} else if (temp.getAttribute("queryID", MOBY_NS) != null) {
			queryID = temp.getAttribute("queryID", MOBY_NS).getValue();
		} else {
			// create a new one -> shouldnt happen very often
			queryID = "a" + queryCount++;
		}

		// now we iterate through all of the direct children called Simple, wrap
		// them individually and set the queryID = queryID
		List list = temp.getChildren("Simple", MOBY_NS);
		if (list.isEmpty()) {
			list = temp.getChildren("Simple");
			if (list.isEmpty()) {
				return new Element[] {};
			}
		}
		// non empty list
		Element[] elements = new Element[list.size()];
		int index = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Element next = (Element) it.next();
			elements[index++] = createMobyDataElementWrapper(next, queryID,
					serviceNotes);
		}
		return elements;
	}

	/**
	 * 
	 * @param message
	 *            the String of xml to extract the list of simples from. This
	 *            method assumes that you are passing in a single invokation and
	 *            that you wish to extract the Simples not contained in any
	 *            collections. This method also maintains any past queryIDs.
	 * @return an array of Strings that represent fully 'wrapped' simples.
	 * @throws MobyException
	 *             if the String doesnt contain a structurally valid Moby
	 *             message structure or if an unexpected error occurs
	 */
	public static String[] getListOfSimples(String message)
			throws MobyException {
		Element element = getDOMDocument(message).getRootElement();
		Element[] elements = getListOfSimples(element);
		String[] strings = new String[elements.length];
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int count = 0; count < elements.length; count++) {
			try {
				strings[count] = outputter.outputString(elements[count]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unexpected error occured while creating String[]:"
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param message
	 *            the String to extract the list of collections from and assumes
	 *            that you are passing in a single invocation message.
	 * @return an array of Strings representing all of the fully 'wrapped'
	 *         collections in the message.
	 * @throws MobyException
	 *             if the the element contains an invalid BioMOBY message
	 */
	public static String[] getListOfCollections(String message)
			throws MobyException {
		Element element = getDOMDocument(message).getRootElement();
		Element[] elements = getListOfCollections(element);
		String[] strings = new String[elements.length];
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int count = 0; count < elements.length; count++) {
			try {
				strings[count] = outputter.outputString(elements[count]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unexpected error occured while creating String[]:"
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param element
	 *            the element to extract the list of collections from and
	 *            assumes that you are passing in a single invocation message.
	 * @return an array of Elements representing all of the fully 'wrapped'
	 *         collections in the message
	 * @throws MobyException
	 *             if the element contains an invalid BioMOBY message
	 */
	public static Element[] getListOfCollections(Element element)
			throws MobyException {
		Element temp = (Element) element.clone();
		Element e = (Element) element.clone();
		String queryID = "";

		if (isMultipleInvocationMessage(e))
			return new Element[] {};

		Element serviceNotes = getServiceNotes(e);

		// if the current elements name isnt MOBY, see if its direct child is
		if (!e.getName().equals("MOBY")) {
			if (e.getChild("MOBY") != null)
				temp = e.getChild("MOBY");
			else if (e.getChild("MOBY", MOBY_NS) != null)
				temp = e.getChild("MOBY", MOBY_NS);
			else
				throw new MobyException(newline
						+ "Expected 'MOBY' as the local name for the element "
						+ newline + "and instead received '" + e.getName()
						+ "' (getListOfCollections(Element element).");
		}
		// parse the mobyContent node
		if (temp.getChild("mobyContent") != null)
			temp = temp.getChild("mobyContent");
		else if (temp.getChild("mobyContent", MOBY_NS) != null)
			temp = temp.getChild("mobyContent", MOBY_NS);
		else
			throw new MobyException(
					newline
							+ "Expected 'mobyContent' as the local name for the next child element but it "
							+ newline
							+ "wasn't there. I even tried a qualified name (getListOfCollections(Element element).");

		// parse the mobyData node
		if (temp.getChild("mobyData") != null) {
			temp = temp.getChild("mobyData");
		} else if (temp.getChild("mobyData", MOBY_NS) != null) {
			temp = temp.getChild("mobyData", MOBY_NS);
		} else {
			throw new MobyException(
					newline
							+ "Expected 'mobyData' as the local name for the next child element but it "
							+ newline
							+ "wasn't there. I even tried a qualified name (getListOfCollections(Element element).");
		}

		// temp == mobyData now we need to get the queryID and save it
		if (temp.getAttribute("queryID") != null) {
			queryID = temp.getAttribute("queryID").getValue();
		} else if (temp.getAttribute("queryID", MOBY_NS) != null) {
			queryID = temp.getAttribute("queryID", MOBY_NS).getValue();
		} else {
			// create a new one -> shouldnt happen very often
			queryID = "a" + queryCount++;
		}

		// now we iterate through all of the direct children called Simple, wrap
		// them individually and set the queryID = queryID
		List list = temp.getChildren("Collection", MOBY_NS);
		if (list.isEmpty()) {
			list = temp.getChildren("Collection");
			if (list.isEmpty()) {
				return new Element[] {};
			}
		}
		// non empty list
		Element[] elements = new Element[list.size()];
		int index = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Element next = (Element) it.next();
			elements[index++] = createMobyDataElementWrapper(next, queryID,
					serviceNotes);
		}
		return elements;
	}

	/**
	 * This method assumes a single invocation was passed to it
	 * <p>
	 * 
	 * @param name
	 *            the article name of the simple that you are looking for
	 * @param xml
	 *            the xml that you want to query
	 * @return a String object that represent the simple found.
	 * @throws MobyException
	 *             if no simple was found given the article name and/or data
	 *             type or if the xml was not valid moby xml or if an unexpected
	 *             error occurs.
	 */
	public static String getSimple(String name, String xml)
			throws MobyException {
		Element element = getDOMDocument(xml).getRootElement();
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		Element simples = getSimple(name, element);
		if (simples != null) {
			try {
				return outputter.outputString(simples);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unexpected error occured while creating String[]:"
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		throw new MobyException(newline + "The simple named '" + name
				+ "' was not found in the xml:" + newline + xml + newline);
	}

	/**
	 * This method assumes a single invocation was passed to it
	 * <p>
	 * 
	 * @param name
	 *            the article name of the simple that you are looking for
	 * @param element
	 *            the Element that you want to query
	 * @return an Element that represents the simple found.
	 * @throws MobyException
	 *             if no simple was found given the article name and/or data
	 *             type or if the xml was not valid moby xml or if an unexpected
	 *             error occurs.
	 */
	public static Element getSimple(String name, Element element)
			throws MobyException {
		Element el = (Element) element.clone();
		Element[] elements = getListOfSimples(el);
		// try matching based on type(less impt) and/or article name (more impt)
		for (int i = 0; i < elements.length; i++) {
			// PRE: elements[i] is a fully wrapped element
			Element e = elements[i];
			if (e.getChild("mobyContent") != null) {
				e = e.getChild("mobyContent");
			} else if (e.getChild("mobyContent", MOBY_NS) != null) {
				e = e.getChild("mobyContent", MOBY_NS);
			} else {
				throw new MobyException(
						newline
								+ "Expected 'mobyContent' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getSimple(String name, "
								+ "Element element).");
			}
			if (e.getChild("mobyData") != null) {
				e = e.getChild("mobyData");
			} else if (e.getChild("mobyData", MOBY_NS) != null) {
				e = e.getChild("mobyData", MOBY_NS);
			} else {
				throw new MobyException(
						newline
								+ "Expected 'mobyData' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getSimple(String name,"
								+ " Element element).");
			}
			if (e.getChild("Simple") != null) {
				e = e.getChild("Simple");
			} else if (e.getChild("Simple", MOBY_NS) != null) {
				e = e.getChild("Simple", MOBY_NS);
			} else {
				throw new MobyException(
						newline
								+ "Expected 'Simple' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getSimple(String name,"
								+ " Element element).");
			}
			// e == Simple -> check its name as long as name != ""
			if (!name.equals(""))
				if (e.getAttributeValue("articleName") != null) {
					String value = e.getAttributeValue("articleName");
					if (value.equals(name)) {
						return e;
					}
				} else if (e.getAttributeValue("articleName", MOBY_NS) != null) {
					String value = e.getAttributeValue("articleName", MOBY_NS);
					if (value.equals(name)) {
						return e;
					}
				}

		}
		throw new MobyException(newline
				+ "The simple named '"
				+ name
				+ "' was not found in the xml:"
				+ newline
				+ (new XMLOutputter(Format.getPrettyFormat()
						.setOmitDeclaration(false))).outputString(element)
				+ newline);
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml containing a single invocation message to
	 *            extract the queryID from
	 * @return the queryID contained in the xml or a generated one if one doesnt
	 *         exist
	 * @throws MobyException
	 *             if the String of xml is invalid or if the message is a
	 *             multiple invocation message
	 */
	public static String getQueryID(String xml) throws MobyException {
		return getQueryID(getDOMDocument(xml).getRootElement());
	}

	/**
	 * 
	 * @param xml
	 *            a single invocation message to extract the queryID from
	 * @return the queryID contained in the xml or a generated one if one doesnt
	 *         exist
	 * @throws if
	 *             the message is a multiple invocation message
	 */
	public static String getQueryID(Element xml) throws MobyException {
		Element temp = (Element) xml.clone();
		Element e = (Element) xml.clone();

		if (isMultipleInvocationMessage(e))
			throw new MobyException(
					"Unable to retrieve the queryID from the BioMOBY message because a message with greater than one IDs exists.");

		if (!e.getName().equals("MOBY")) {
			if (e.getChild("MOBY") != null)
				temp = e.getChild("MOBY");
			else if (e.getChild("MOBY", MOBY_NS) != null)
				temp = e.getChild("MOBY", MOBY_NS);
		}
		// parse the mobyContent node
		if (temp.getChild("mobyContent") != null)
			temp = temp.getChild("mobyContent");
		else if (temp.getChild("mobyContent", MOBY_NS) != null)
			temp = temp.getChild("mobyContent", MOBY_NS);

		// parse the mobyData node
		if (temp.getChild("mobyData") != null) {
			temp = temp.getChild("mobyData");
		} else if (temp.getChild("mobyData", MOBY_NS) != null) {
			temp = temp.getChild("mobyData", MOBY_NS);
		}

		// temp == mobyData now we need to get the queryID and save it
		if (temp.getAttribute("queryID") != null) {
			return temp.getAttribute("queryID").getValue();
		} else if (temp.getAttribute("queryID", MOBY_NS) != null) {
			return temp.getAttribute("queryID", MOBY_NS).getValue();
		} else {
			// create a new one -> shouldnt happen very often
			return "a" + queryCount++;
		}
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml containing a single invocation message to
	 *            extract the queryID from
	 * @return the element passed in to the method with the queryID set if the
	 *         message was valid.
	 * @throws MobyException
	 *             if the String of xml is syntatically invalid or if the
	 *             message is a multiple invocation message
	 */
	public static String setQueryID(String xml, String id) throws MobyException {
		return new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(
				false)).outputString(setQueryID(getDOMDocument(xml)
				.getRootElement(), id));
	}

	/**
	 * 
	 * @param xml
	 *            a single invocation message to extract the queryID from
	 * @return the element passed in to the method with the queryID set if the
	 *         message was valid.
	 * @throws MobyException
	 *             if the message is a multiple invocation message
	 */
	public static Element setQueryID(Element xml, String id)
			throws MobyException {
		Element e = (Element) xml.clone();
		Element temp = e;
		if (isMultipleInvocationMessage(e))
			throw new MobyException(
					"Unable to set the queryID, because there are more than one queryID to set!");
		if (!e.getName().equals("MOBY")) {
			if (e.getChild("MOBY") != null)
				temp = e.getChild("MOBY");
			else if (e.getChild("MOBY", MOBY_NS) != null)
				temp = e.getChild("MOBY", MOBY_NS);
		}
		// parse the mobyContent node
		if (temp.getChild("mobyContent") != null)
			temp = temp.getChild("mobyContent");
		else if (temp.getChild("mobyContent", MOBY_NS) != null)
			temp = temp.getChild("mobyContent", MOBY_NS);

		// parse the mobyData node
		if (temp.getChild("mobyData") != null) {
			temp = temp.getChild("mobyData");
		} else if (temp.getChild("mobyData", MOBY_NS) != null) {
			temp = temp.getChild("mobyData", MOBY_NS);
		}

		temp.removeAttribute("queryID");
		temp.removeAttribute("queryID", MOBY_NS);
		temp.setAttribute("queryID", (id == null || id == "" ? "a"
				+ queryCount++ : id), MOBY_NS);
		return e;
	}

	/**
	 * 
	 * @param name
	 *            the articlename of the simple that you wish to extract
	 * @param xml
	 *            the xml message
	 * @return the wrapped simple if it exists
	 * @throws MobyException
	 *             if the message is a multiple invocation message or if the xml
	 *             is syntatically invalid.
	 */
	public static String getWrappedSimple(String name, String xml)
			throws MobyException {
		Element element = getWrappedSimple(name, getDOMDocument(xml)
				.getRootElement());
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return outputter.outputString(element);
	}

	/**
	 * 
	 * @param name
	 *            the articlename of the simple that you wish to extract
	 * @param xml
	 *            the xml message
	 * @return the wrapped simple if it exists
	 * @throws MobyException
	 *             if the message is a multiple invocation message.
	 */
	public static Element getWrappedSimple(String name, Element element)
			throws MobyException {
		Element e = (Element) element.clone();
		String queryID = getQueryID(e);
		Element serviceNotes = getServiceNotes(e);
		Element simple = getSimple(name, e);
		return createMobyDataElementWrapper(simple, queryID, serviceNotes);
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract
	 * @param element
	 *            the element to extract the collection from
	 * @return the collection if found
	 * @throws MobyException
	 *             if the message is invalid
	 */
	public static Element getCollection(String name, Element element)
			throws MobyException {
		Element el = (Element) element.clone();
		Element[] elements = getListOfCollections(el);
		for (int i = 0; i < elements.length; i++) {
			// PRE: elements[i] is a fully wrapped element
			Element e = elements[i];
			if (e.getChild("mobyContent") != null) {
				e = e.getChild("mobyContent");
			} else if (e.getChild("mobyContent", MOBY_NS) != null) {
				e = e.getChild("mobyContent", MOBY_NS);
			} else {
				throw new MobyException(
						newline
								+ "Expected 'mobyContent' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getCollection(String name, "
								+ "Element element).");
			}
			if (e.getChild("mobyData") != null) {
				e = e.getChild("mobyData");
			} else if (e.getChild("mobyData", MOBY_NS) != null) {
				e = e.getChild("mobyData", MOBY_NS);
			} else {
				throw new MobyException(
						newline
								+ "Expected 'mobyData' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getCollection(String name,"
								+ " Element element).");
			}
			if (e.getChild("Collection") != null) {
				e = e.getChild("Collection");
			} else if (e.getChild("Collection", MOBY_NS) != null) {
				e = e.getChild("Collection", MOBY_NS);
			} else {
				// TODO should i throw exception or continue?
				throw new MobyException(
						newline
								+ "Expected 'Collection' as the local name for the next child element but it "
								+ newline
								+ "wasn't there. I even tried a qualified name (getCollection(String name,"
								+ " Element element).");
			}
			// e == collection -> check its name
			if (e.getAttributeValue("articleName") != null) {
				String value = e.getAttributeValue("articleName");
				if (value.equals(name)) {
					return e;
				}
			} else if (e.getAttributeValue("articleName", MOBY_NS) != null) {
				String value = e.getAttributeValue("articleName", MOBY_NS);
				if (value.equals(name)) {
					return e;
				}
			}
			if (elements.length == 1) {
				if (e.getAttributeValue("articleName") != null) {
					String value = e.getAttributeValue("articleName");
					if (value.equals("")) {
						// rename it to make it compatible with moby
						e.setAttribute("articleName", name, MOBY_NS);
						return e;
					}
				} else if (e.getAttributeValue("articleName", MOBY_NS) != null) {
					String value = e.getAttributeValue("articleName", MOBY_NS);
					if (value.equals("")) {
						// rename it to make it compatible with moby
						e.setAttribute("articleName", name, MOBY_NS);
						return e;
					}
				}
			}
			// name didnt match, so too bad ;-)
		}
		throw new MobyException(
				newline
						+ "The Collection named '"
						+ name
						+ "' was not found in the xml:"
						+ newline
						+ (new XMLOutputter(Format.getPrettyFormat()
								.setOmitDeclaration(false)))
								.outputString(element)
						+ newline
						+ "Note: A collection of that may exist, but may be contained in a multiple invocation message.");
	}

	/**
	 * 
	 * @param name
	 * @param xml
	 * @return
	 * @throws MobyException
	 */
	public static String getCollection(String name, String xml)
			throws MobyException {
		Element element = getDOMDocument(xml).getRootElement();
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		Element collection = getCollection(name, element);
		if (collection != null)
			return outputter.outputString(collection);
		return null;
	}

	/**
	 * 
	 * @param name
	 * @param element
	 * @return
	 * @throws MobyException
	 */
	public static Element getWrappedCollection(String name, Element element)
			throws MobyException {
		Element e = (Element) element.clone();
		String queryID = getQueryID(e);
		Element collection = getCollection(name, e);
		Element serviceNotes = getServiceNotes(e);
		return createMobyDataElementWrapper(collection, queryID, serviceNotes);
	}

	/**
	 * 
	 * @param name
	 * @param xml
	 * @return
	 * @throws MobyException
	 */
	public static String getWrappedCollection(String name, String xml)
			throws MobyException {
		Element element = getWrappedCollection(name, getDOMDocument(xml)
				.getRootElement());
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return outputter.outputString(element);
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract the simples from.
	 * @param xml
	 *            the XML to extract from
	 * @return an array of String objects that represent the simples
	 * @throws MobyException
	 */
	public static String[] getSimplesFromCollection(String name, String xml)
			throws MobyException {
		Element[] elements = getSimplesFromCollection(name, getDOMDocument(xml)
				.getRootElement());
		String[] strings = new String[elements.length];
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int i = 0; i < elements.length; i++) {
			try {
				strings[i] = output.outputString(elements[i]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unknown error occured while creating String[]."
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract the simples from.
	 * @param element
	 *            the Element to extract from
	 * @return an array of Elements objects that represent the simples
	 * @throws MobyException
	 */
	public static Element[] getSimplesFromCollection(String name,
			Element element) throws MobyException {
		Element e = (Element) element.clone();
		// exception thrown if not found
		Element collection = getCollection(name, e);

		List list = collection.getChildren("Simple");
		if (list.isEmpty())
			list = collection.getChildren("Simple", MOBY_NS);
		if (list.isEmpty())
			return new Element[] {};
		Vector vector = new Vector();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof Element) {
				((Element) o).setAttribute("articleName", name, MOBY_NS);
				if (((Element) o).getChildren().size() > 0)
					vector.add(o);
			}

		}
		Element[] elements = new Element[vector.size()];
		vector.copyInto(elements);
		return elements;
	}

	/**
	 * 
	 * @param name
	 *            the name of the simples that you would like to extract. The
	 *            name can be collection name as well. This method extracts
	 *            simples from all invocation messages.
	 * @param xml
	 *            the xml to extract the simples from
	 * @return a String[] of Simples that you are looking for, taken from
	 *         collections with your search name as well as simple elements with
	 *         the search name
	 * @throws MobyException
	 *             if there is a problem with the BioMOBY message
	 */
	public static String[] getAllSimplesByArticleName(String name, String xml)
			throws MobyException {
		Element[] elements = getAllSimplesByArticleName(name, getDOMDocument(
				xml).getRootElement());
		String[] strings = new String[elements.length];
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int i = 0; i < elements.length; i++) {
			try {
				strings[i] = output.outputString(elements[i]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unknown error occured while creating String[]."
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param name
	 *            the name of the simples that you would like to extract. The
	 *            name can be collection name as well. This method extracts
	 *            simples from all invocation messages.
	 * @param element
	 *            the xml to extract the simples from
	 * @return a String[] of Simples that you are looking for, taken from
	 *         collections with your search name as well as simple elements with
	 *         the search name
	 * @throws MobyException
	 *             if there is a problem with the BioMOBY message
	 */
	public static Element[] getAllSimplesByArticleName(String name,
			Element element) throws MobyException {
		Element e = (Element) element.clone();
		Element[] invocations = getSingleInvokationsFromMultipleInvokations(e);
		Vector vector = new Vector();
		for (int i = 0; i < invocations.length; i++) {
			Element collection = null;
			try {
				collection = getCollection(name, invocations[i]);
			} catch (MobyException me) {

			}
			if (collection != null) {
				List list = collection.getChildren("Simple");
				if (list.isEmpty())
					list = collection.getChildren("Simple", MOBY_NS);
				if (list.isEmpty())
					return new Element[] {};
				for (Iterator it = list.iterator(); it.hasNext();) {
					Object o = it.next();
					if (o instanceof Element) {
						((Element) o)
								.setAttribute("articleName", name, MOBY_NS);
					}
					vector.add(o);
				}
			}
			collection = null;

			Element[] potentialSimples = getListOfSimples(invocations[i]);
			for (int j = 0; j < potentialSimples.length; j++) {
				Element mobyData = extractMobyData(potentialSimples[j]);
				Element simple = mobyData.getChild("Simple");
				if (simple == null)
					simple = mobyData.getChild("Simple", MOBY_NS);
				if (simple != null) {
					if (simple.getAttribute("articleName") != null) {
						if (simple.getAttribute("articleName").getValue()
								.equals(name))
							vector.add(simple);
					} else if (simple.getAttribute("articleName", MOBY_NS) != null) {
						if (simple.getAttribute("articleName", MOBY_NS)
								.getValue().equals(name))
							vector.add(simple);
					}
				}

			}
		}

		Element[] elements = new Element[vector.size()];
		vector.copyInto(elements);
		return elements;
	}

	/**
	 * 
	 * @param xml
	 *            the XML to extract from
	 * @return an array of String objects that represent the simples
	 * @throws MobyException
	 */
	public static String[] getSimplesFromCollection(String xml)
			throws MobyException {
		Element[] elements = getSimplesFromCollection(getDOMDocument(xml)
				.getRootElement());
		String[] strings = new String[elements.length];
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int i = 0; i < elements.length; i++) {
			try {
				strings[i] = output.outputString(elements[i]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unknown error occured while creating String[]."
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract the simples from.
	 * @param element
	 *            the Element to extract from
	 * @return an array of Elements objects that represent the 'unwrapped'
	 *         simples
	 * @throws MobyException
	 */
	public static Element[] getSimplesFromCollection(Element element)
			throws MobyException {
		Element e = (Element) element.clone();
		Element mobyData = extractMobyData(e);

		Element collection = mobyData.getChild("Collection");
		if (collection == null)
			collection = mobyData.getChild("Collection", MOBY_NS);

		List list = collection.getChildren("Simple");
		if (list.isEmpty())
			list = collection.getChildren("Simple", MOBY_NS);
		if (list.isEmpty())
			return new Element[] {};
		Vector vector = new Vector();
		for (Iterator it = list.iterator(); it.hasNext();) {
			vector.add(it.next());
		}
		Element[] elements = new Element[vector.size()];
		vector.copyInto(elements);
		return elements;
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract the simples from.
	 * @param xml
	 *            the XML to extract from
	 * @return an array of String objects that represent the simples, with the
	 *         name of the collection
	 * @throws MobyException
	 *             if the collection doesnt exist or the xml is invalid
	 */
	public static String[] getWrappedSimplesFromCollection(String name,
			String xml) throws MobyException {
		Element[] elements = getWrappedSimplesFromCollection(name,
				getDOMDocument(xml).getRootElement());
		String[] strings = new String[elements.length];
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		for (int i = 0; i < elements.length; i++) {
			try {
				strings[i] = output.outputString(elements[i]);
			} catch (Exception e) {
				throw new MobyException(newline
						+ "Unknown error occured while creating String[]."
						+ newline + Utils.format(e.getLocalizedMessage(), 3));
			}
		}
		return strings;
	}

	/**
	 * 
	 * @param name
	 *            the name of the collection to extract the simples from.
	 * @param element
	 *            the Element to extract from
	 * @return an array of Elements objects that represent the simples, with the
	 *         name of the collection
	 * @throws MobyException
	 *             MobyException if the collection doesnt exist or the xml is
	 *             invalid
	 */
	public static Element[] getWrappedSimplesFromCollection(String name,
			Element element) throws MobyException {
		Element el = (Element) element.clone();
		String queryID = getQueryID(el);
		Element collection = getCollection(name, el);
		Element serviceNotes = getServiceNotes(el);
		List list = collection.getChildren("Simple");
		if (list.isEmpty())
			list = collection.getChildren("Simple", MOBY_NS);
		if (list.isEmpty())
			return new Element[] {};
		Vector vector = new Vector();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Element e = (Element) it.next();
			e.setAttribute("articleName", name, MOBY_NS);
			e = createMobyDataElementWrapper(e, queryID + "_split"
					+ queryCount++, serviceNotes);
			vector.add(e);
		}
		Element[] elements = new Element[vector.size()];
		vector.copyInto(elements);
		return elements;
	}

	/**
	 * 
	 * @param xml
	 *            the message to extract the invocation messages from
	 * @return an array of String objects each representing a distinct BioMOBY
	 *         invocation message.
	 * @throws MobyException
	 *             if the moby message is invalid or if the xml is syntatically
	 *             invalid.
	 */
	public static String[] getSingleInvokationsFromMultipleInvokations(
			String xml) throws MobyException {
		Element[] elements = getSingleInvokationsFromMultipleInvokations(getDOMDocument(
				xml).getRootElement());
		String[] strings = new String[elements.length];
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));

		for (int i = 0; i < elements.length; i++) {
			strings[i] = output.outputString(new Document(elements[i]));
		}
		return strings;
	}

	/**
	 * 
	 * @param element
	 *            the message to extract the invocation messages from
	 * @return an array of Element objects each representing a distinct
	 *         invocation message.
	 * @throws MobyException
	 *             if the moby message is invalid.
	 */
	public static Element[] getSingleInvokationsFromMultipleInvokations(
			Element element) throws MobyException {
		Element e = (Element) element.clone();
		Element serviceNotes = getServiceNotes(e);
		if (e.getChild("MOBY") != null) {
			e = e.getChild("MOBY");
		} else if (e.getChild("MOBY", MOBY_NS) != null) {
			e = e.getChild("MOBY", MOBY_NS);
		}

		if (e.getChild("mobyContent") != null) {
			e = e.getChild("mobyContent");
		} else if (e.getChild("mobyContent", MOBY_NS) != null) {
			e = e.getChild("mobyContent", MOBY_NS);
		} else {
			throw new MobyException(
					newline
							+ "Expected a child element called 'mobyContent' and did not receive it in:"
							+ newline
							+ new XMLOutputter(Format.getPrettyFormat()
									.setOmitDeclaration(false)).outputString(e));
		}
		List invocations = e.getChildren("mobyData");
		if (invocations.isEmpty())
			invocations = e.getChildren("mobyData", MOBY_NS);
		Element[] elements = new Element[] {};
		ArrayList theData = new ArrayList();
		for (Iterator it = invocations.iterator(); it.hasNext();) {
			Element MOBY = new Element("MOBY", MOBY_NS);
			Element mobyContent = new Element("mobyContent", MOBY_NS);
			if (serviceNotes != null)
				mobyContent.addContent(serviceNotes.detach());
			Element mobyData = new Element("mobyData", MOBY_NS);
			Element next = (Element) it.next();
			String queryID = next.getAttributeValue("queryID", MOBY_NS);
			if (queryID == null)
				queryID = next.getAttributeValue("queryID");

			mobyData.setAttribute("queryID", (queryID == null ? "a"+queryCount++ : queryID), MOBY_NS);
			mobyData.addContent(next.cloneContent());
			MOBY.addContent(mobyContent);
			mobyContent.addContent(mobyData);
			if (next.getChildren().size() > 0)
				theData.add(MOBY);
		}
		elements = new Element[theData.size()];
		elements = (Element[]) theData.toArray(elements);
		return elements;
	}

	/**
	 * 
	 * @param document
	 *            the string to create a DOM document from
	 * @return a Document object that represents the string of XML.
	 * @throws MobyException
	 *             if the xml is invalid syntatically.
	 */
	public static Document getDOMDocument(String document) throws MobyException {
		if (document == null)
			throw new MobyException(newline
					+ "null found where an XML document was expected.");
		SAXBuilder builder = new SAXBuilder();
		// Create the document
		Document doc = null;
		try {
			doc = builder.build(new StringReader(document));
		} catch (JDOMException e) {
			throw new MobyException(newline + "Error parsing XML:->" + newline
					+ document + newline
					+ Utils.format(e.getLocalizedMessage(), 3) + ".");
		} catch (IOException e) {
			throw new MobyException(newline + "Error parsing XML:->" + newline
					+ Utils.format(e.getLocalizedMessage(), 3) + ".");
		} catch (Exception e) {
			throw new MobyException(newline + "Error parsing XML:->" + newline
					+ Utils.format(e.getLocalizedMessage(), 3) + ".");
		}
		return doc;
	}

	/**
	 * 
	 * @param elements
	 *            the fully wrapped moby simples and/or collections to wrap an
	 *            input message around
	 * @param queryID
	 *            the queryID for this input
	 * @return a fully wrapped message with an appropriate queryID and elements
	 *         added to it
	 * @throws MobyException
	 *             if an element is invalid or if the XML is syntatically
	 *             invalid.
	 */
	public static String createServiceInput(String[] elements, String queryID)
			throws MobyException {
		Element[] element = new Element[elements.length];
		for (int i = 0; i < elements.length; i++) {
			element[i] = getDOMDocument(elements[i]).getRootElement();
		}
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return output.outputString(createServiceInput(element, queryID));
	}

	/**
	 * 
	 * @param elements
	 *            the fully wrapped moby simples and/or collections to wrap an
	 *            input message around
	 * @param queryID
	 *            the queryID for this input
	 * @return a fully wrapped message with an appropriate queryID and elements
	 *         added to it
	 * @throws MobyException
	 *             if an element is invalid.
	 */
	public static Element createServiceInput(Element[] elements, String queryID)
			throws MobyException {
		// create the main elements
		Element MOBY = new Element("MOBY", MOBY_NS);
		Element mobyContent = new Element("mobyContent", MOBY_NS);
		Element mobyData = new Element("mobyData", MOBY_NS);
		mobyData.setAttribute("queryID", (queryID == null ? "" : queryID),
				MOBY_NS);

		// add the content
		MOBY.addContent(mobyContent);
		mobyContent.addContent(mobyData);

		// iterate through elements adding the content of mobyData
		for (int i = 0; i < elements.length; i++) {
			Element e = (Element) elements[i].clone();
			e = extractMobyData(e);
			mobyData.addContent(e.cloneContent());
		}

		return MOBY;
	}

	/**
	 * @param element
	 *            the element that contains the moby message that you would like
	 *            to extract the mobyData block from (assumes single invocation,
	 *            but returns the first mobyData block in a multiple invocation
	 *            message).
	 * @return the mobyData element block.
	 * @throws MobyException
	 *             if the moby message is invalid
	 */
	public static Element extractMobyData(Element element) throws MobyException {
		Element e = (Element) element.clone();
		if (e.getChild("MOBY") != null) {
			e = e.getChild("MOBY");
		} else if (e.getChild("MOBY", MOBY_NS) != null) {
			e = e.getChild("MOBY", MOBY_NS);
		}

		if (e.getChild("mobyContent") != null) {
			e = e.getChild("mobyContent");
		} else if (e.getChild("mobyContent", MOBY_NS) != null) {
			e = e.getChild("mobyContent", MOBY_NS);
		} else {
			throw new MobyException(
					newline
							+ "Expected the child element 'mobyContent' and did not receive it in:"
							+ newline
							+ new XMLOutputter(Format.getPrettyFormat()
									.setOmitDeclaration(false)).outputString(e));
		}

		if (e.getChild("mobyData") != null) {
			e = e.getChild("mobyData");
		} else if (e.getChild("mobyData", MOBY_NS) != null) {
			e = e.getChild("mobyData", MOBY_NS);
		} else {
			throw new MobyException(
					newline
							+ "Expected the child element 'mobyData' and did not receive it in:"
							+ newline
							+ new XMLOutputter(Format.getPrettyFormat()
									.setOmitDeclaration(false)).outputString(e));
		}
		return e;
	}

	/**
	 * 
	 * @param newName
	 *            the new name for this fully wrapped BioMOBY collection
	 * @param element
	 *            the fully wrapped BioMOBY collection
	 * @return @return an element 'Collection' representing the renamed collection
	 * @throws MobyException
	 *             if the message is invalid
	 */
	public static Element renameCollection(String newName, Element element)
			throws MobyException {
		Element e = (Element) element.clone();
		Element mobyData = extractMobyData(e);
		Element coll = mobyData.getChild("Collection");
		if (coll == null)
			coll = mobyData.getChild("Collection", MOBY_NS);
		if (coll == null)
			return e;
		coll.removeAttribute("articleName");
		coll.removeAttribute("articleName", MOBY_NS);
		coll.setAttribute("articleName", newName, MOBY_NS);
		return coll;
	}

	/**
	 * 
	 * @param newName
	 *            the new name for this fully wrapped BioMOBY collection
	 * @param xml
	 *            the fully wrapped BioMOBY collection
	 * @return an element 'Collection' representing the renamed collection
	 * @throws MobyException
	 *             if the BioMOBY message is invalid or the xml is syntatically
	 *             invalid.
	 */
	public static String renameCollection(String newName, String xml)
			throws MobyException {
		return new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(
				false)).outputString(renameCollection(newName, getDOMDocument(
				xml).getRootElement()));
	}

	/**
	 * 
	 * @param oldName
	 * @param newName
	 * @param type
	 * @param xml
	 * @return
	 * @throws MobyException
	 */
	public static String renameSimple(String newName, String type, String xml)
			throws MobyException {
		return new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(
				false)).outputString(renameSimple(newName, type,
				getDOMDocument(xml).getRootElement()));
	}

	/**
	 * 
	 * @param oldName
	 * @param newName
	 * @param type
	 * @param element
	 * @return
	 * @throws MobyException
	 */
	public static Element renameSimple(String newName, String type,
			Element element) throws MobyException {
		Element e = (Element) element.clone();
		Element mobyData = extractMobyData(e);
		String queryID = getQueryID(e);
		Element serviceNotes = getServiceNotes(e);
		Element simple = mobyData.getChild("Simple");
		if (simple == null)
			simple = mobyData.getChild("Simple", MOBY_NS);
		if (simple == null) {
			return e;
		}
		simple.removeAttribute("articleName");
		simple.removeAttribute("articleName", MOBY_NS);
		simple.setAttribute("articleName", newName, MOBY_NS);
		return createMobyDataElementWrapper(simple, queryID, serviceNotes);
	}

	/**
	 * 
	 * @return
	 * @throws MobyException
	 */
	public static Document createDomDocument() throws MobyException {
		Document d = new Document();
		d.setBaseURI(MOBY_NS.getURI());
		return d;
	}

	/**
	 * 
	 * @param element
	 * @param queryID
	 * @param serviceNotes
	 * @return
	 * @throws MobyException
	 */
	public static Element createMobyDataElementWrapper(Element element,
			String queryID, Element serviceNotes) throws MobyException {
		Element e = (Element) element.clone();
		Element MOBY = new Element("MOBY", MOBY_NS);
		Element mobyContent = new Element("mobyContent", MOBY_NS);
		Element mobyData = new Element("mobyData", MOBY_NS);
		mobyData.setAttribute("queryID", queryID, MOBY_NS);
		MOBY.addContent(mobyContent);
		mobyContent.addContent(mobyData);
		// add the serviceNotes if they exist
		if (serviceNotes != null)
			mobyContent.addContent(serviceNotes.detach());

		if (e != null) {
			if (e.getName().equals("Simple")) {
				Element simple = new Element("Simple", MOBY_NS);
				simple.setAttribute("articleName", (e
						.getAttributeValue("articleName") == null ? e
						.getAttributeValue("articleName", MOBY_NS, "") : e
						.getAttributeValue("articleName", "")), MOBY_NS);
				simple.addContent(e.cloneContent());
				if (simple.getChildren().size() > 0)
					mobyData.addContent(simple.detach());
			} else if (e.getName().equals("Collection")) {
				Element collection = new Element("Collection", MOBY_NS);
				collection.setAttribute("articleName", (e
						.getAttributeValue("articleName") == null ? e
						.getAttributeValue("articleName", MOBY_NS, "") : e
						.getAttributeValue("articleName", "")), MOBY_NS);
				collection.addContent(e.cloneContent());
				if (collection.getChildren().size() > 0)
					mobyData.addContent(collection.detach());
			}
		}

		return MOBY;
	}

	public static Element createMobyDataWrapper(String queryID,
			Element serviceNotes) throws MobyException {

		Element e = null;

		if (serviceNotes != null)
			e = (Element) serviceNotes.clone();

		Element MOBY = new Element("MOBY", MOBY_NS);
		Element mobyContent = new Element("mobyContent", MOBY_NS);
		if (e != null)
			mobyContent.addContent(e.detach());
		Element mobyData = new Element("mobyData", MOBY_NS);
		mobyData.setAttribute("queryID", queryID, MOBY_NS);
		MOBY.addContent(mobyContent);
		mobyContent.addContent(mobyData);
		return MOBY;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 * @throws MobyException
	 */
	public static String createMobyDataElementWrapper(String xml)
			throws MobyException {
		return createMobyDataElementWrapper(xml, "a" + queryCount++);
	}

	/**
	 * 
	 * @param element
	 * @return
	 * @throws MobyException
	 */
	public static Element createMobyDataElementWrapper(Element element)
			throws MobyException {
		Element serviceNotes = getServiceNotes((Element) element.clone());
		return createMobyDataElementWrapper(element, "a" + queryCount++,
				serviceNotes);
	}

	/**
	 * 
	 * @param xml
	 * @param queryID
	 * @return
	 * @throws MobyException
	 */
	public static String createMobyDataElementWrapper(String xml, String queryID)
			throws MobyException {
		if (xml == null)
			return null;
		Element serviceNotes = getServiceNotes(getDOMDocument(xml)
				.getRootElement());
		Element element = createMobyDataElementWrapper(getDOMDocument(xml)
				.getRootElement(), queryID, serviceNotes);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return (element == null ? null : outputter.outputString(element));
	}

	public static String createMobyDataElementWrapper(String xml,
			String queryID, Element serviceNotes) throws MobyException {
		if (xml == null)
			return null;
		Element element = createMobyDataElementWrapper(getDOMDocument(xml)
				.getRootElement(), queryID, serviceNotes);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return (element == null ? null : outputter.outputString(element));
	}

	/**
	 * 
	 * @param elements
	 * @return
	 * @throws MobyException
	 */
	public static Element createMultipleInvokations(Element[] elements)
			throws MobyException {
		Element MOBY = new Element("MOBY", MOBY_NS);
		Element mobyContent = new Element("mobyContent", MOBY_NS);
		Element serviceNotes = null;
		for (int i = 0; i < elements.length; i++) {
			if (serviceNotes == null) {
				serviceNotes = getServiceNotes((Element) elements[i].clone());
				if (serviceNotes != null)
					mobyContent.addContent(serviceNotes.detach());
			}
			Element mobyData = new Element("mobyData", MOBY_NS);
			Element md = extractMobyData((Element) elements[i].clone());
			String queryID = getQueryID((Element) elements[i].clone());
			mobyData.setAttribute("queryID", queryID, MOBY_NS);
			mobyData.addContent(md.cloneContent());
			mobyContent.addContent(mobyData);
		}
		MOBY.addContent(mobyContent);

		return MOBY;
	}

	/**
	 * 
	 * @param xmls
	 * @return
	 * @throws MobyException
	 */
	public static String createMultipleInvokations(String[] xmls)
			throws MobyException {
		Element[] elements = new Element[xmls.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = getDOMDocument(xmls[i]).getRootElement();
		}
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat()
				.setOmitDeclaration(false));
		return output.outputString(createMultipleInvokations(elements));
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml
	 * @return true if the xml contains a full moby message (assumes single
	 *         invocation, but will return the first mobyData block from a
	 *         multiple invocation message).
	 */
	public static boolean isWrapped(Element element) {
		try {
			extractMobyData((Element) element.clone());
			return true;
		} catch (MobyException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml
	 * @return true if the xml contains a full moby message (assumes single
	 *         invocation, but will return the first mobyData block from a
	 *         multiple invocation message).
	 * @throws MobyException
	 *             if the xml is syntatically invalid
	 */
	public static boolean isWrapped(String xml) throws MobyException {
		Element element = getDOMDocument(xml).getRootElement();
		return isWrapped(element);
	}

	/**
	 * 
	 * @param element
	 *            an Element containing a single invocation
	 * @return true if the element contains a moby collection, false otherwise.
	 * @throws MobyException
	 *             if xml is invalid
	 */
	public static boolean isCollection(Element element) throws MobyException {
		try {
			return getListOfCollections((Element) element.clone()).length > 0;

		} catch (MobyException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml containing a single invocation
	 * @return true if the xml contains a moby collection, false otherwise.
	 * @throws MobyException
	 *             if xml is invalid
	 */
	public static boolean isCollection(String xml) throws MobyException {
		Element element = getDOMDocument(xml).getRootElement();
		return isCollection(element);
	}

	/**
	 * 
	 * @param xml
	 *            a string of xml to check for emptiness
	 * @return true if the element is empty, false otherwise.
	 */
	public static boolean isEmpty(String xml) {
		try {
			return isEmpty(getDOMDocument(xml).getRootElement());
		} catch (MobyException e) {
			return true;
		}
	}

	/**
	 * 
	 * @param xml
	 *            an element to check for emptiness
	 * @return true if the element is empty, false otherwise.
	 */
	public static boolean isEmpty(Element xml) {
		try {
			Element e = extractMobyData((Element) xml.clone());
			if (e.getChild("Collection") != null)
				return false;
			if (e.getChild("Collection", MOBY_NS) != null)
				return false;
			if (e.getChild("Simple") != null)
				return false;
			if (e.getChild("Simple", MOBY_NS) != null)
				return false;
		} catch (MobyException e) {
		}
		return true;

	}

	/**
	 * 
	 * @param theList
	 *            a list of Elements that represent collections (wrapped in a
	 *            MobyData tag
	 * @param name
	 *            the name to set for the collection
	 * @return a list containing a single wrapped collection Element that contains all
	 *         of the simples in the collections in theList
	 * @throws MobyException
	 * 
	 */
	public static List mergeCollections(List theList, String name)
			throws MobyException {
		if (theList == null)
			return new ArrayList();
		Element mainCollection = new Element("Collection", MOBY_NS);
		mainCollection.setAttribute("articleName", name, MOBY_NS);
		String queryID = "";
		for (Iterator iter = theList.iterator(); iter.hasNext();) {
			Element mobyData = (Element) iter.next();
			queryID = getQueryID(mobyData);
			Element collection = mobyData.getChild("Collection");
			if (collection == null)
				collection = mobyData.getChild("Collection", MOBY_NS);
			if (collection == null)
				continue;
			mainCollection.addContent(collection.cloneContent());
		}
		theList = new ArrayList();
		theList
				.add((createMobyDataElementWrapper(mainCollection, queryID,
						null)));
		return theList;
	}

	/**
	 * 
	 * @param element
	 *            a full moby message (root element called MOBY) and may be
	 *            prefixed
	 * @return the serviceNotes element if it exists, null otherwise.
	 */
	public static Element getServiceNotes(Element element) {
		Element serviceNotes = null;
		Element e = (Element) element.clone();
		Element mobyContent = e.getChild("mobyContent");
		if (mobyContent == null)
			mobyContent = e.getChild("mobyContent", MOBY_NS);

		// should throw exception?
		if (mobyContent == null)
			return serviceNotes;

		serviceNotes = mobyContent.getChild("serviceNotes");
		if (serviceNotes == null)
			serviceNotes = mobyContent.getChild("serviceNotes", MOBY_NS);
		// note: servicenotes may be null
		return serviceNotes;
	}

	/**
	 * 
	 * @param xml
	 *            a full moby message (root element called MOBY) and may be
	 *            prefixed
	 * @return the serviceNotes element as a string if it exists, null
	 *         otherwise.
	 */
	public static String getServiceNotes(String xml) {
		try {
			Element e = getServiceNotes(getDOMDocument(xml).getRootElement());
			if (e == null)
				return null;
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat()
					.setOmitDeclaration(false));
			return out.outputString(e);
		} catch (MobyException ex) {
			return null;
		}
	}

	/**
	 * 
	 * @param xml
	 *            a full moby message (root element called MOBY) and may be
	 *            prefixed
	 * @return the serviceNotes element if it exists, null otherwise.
	 */
	public static Element getServiceNotesAsElement(String xml) {
		try {
			Element e = getServiceNotes(getDOMDocument(xml).getRootElement());
			return e;
		} catch (MobyException ex) {
			return null;
		}
	}

	/**
	 * 
	 * @param element
	 *            the xml element
	 * @param articleName
	 *            the name of the child to extract
	 * @return an element that represents the direct child or null if it wasnt
	 *         found.
	 */
	public static Element getDirectChildByArticleName(Element element,
			String articleName) {
		Element e = (Element) element.clone();
		List list = e.getChildren();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object object = iter.next();
			if (object instanceof Element) {
				Element child = (Element) object;
				if (child.getAttributeValue("articleName") != null) {
					if (child.getAttributeValue("articleName").equals(
							articleName))
						return child;
				} else if (child.getAttributeValue("articleName", MOBY_NS) != null) {
					if (child.getAttributeValue("articleName", MOBY_NS).equals(
							articleName)) {
						return child;
					}
				}
			}

		}
		return null;

	}

	/**
	 * 
	 * @param xml
	 *            the string of xml
	 * @param articleName
	 *            the name of the child to extract
	 * @return an xml string that represents the direct child or null if it
	 *         wasnt found.
	 */
	public static String getDirectChildByArticleName(String xml,
			String articleName) {
		try {
			Element e = getDirectChildByArticleName(getDOMDocument(xml)
					.getRootElement(), articleName);
			if (e == null)
				return null;
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat()
					.setOmitDeclaration(false));
			return out.outputString(e);
		} catch (MobyException me) {
			return null;
		}
	}

	/**
	 * 
	 * @param xml
	 *            the xml message to test whether or not there is stuff in the
	 *            mobyData portion of a message.
	 * @return true if there is data, false otherwise.
	 */
	public static boolean isThereData(Element xml) {
		Element e = null;
		e = (Element) xml.clone();
		try {
			e = extractMobyData(e);
			if (e.getChildren().size() > 0) {
				// make sure we dont have empty collections or simples
				if (e.getChild("Collection") != null) {
					return e.getChild("Collection").getChildren().size() > 0;
				}
				if (e.getChild("Collection", MOBY_NS) != null) {
					return e.getChild("Collection", MOBY_NS).getChildren()
							.size() > 0;
				}
				if (e.getChild("Simple") != null) {
					return e.getChild("Simple").getChildren().size() > 0;
				}
				if (e.getChild("Simple", MOBY_NS) != null) {
					return e.getChild("Simple", MOBY_NS).getChildren().size() > 0;
				}
				return false;
			}
		} catch (MobyException e1) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param xml
	 *            the xml message to test whether or not there is stuff in the
	 *            mobyData portion of a message.
	 * @return true if there is data, false otherwise.
	 */
	public static boolean isThereData(String xml) {
		try {
			return isThereData(getDOMDocument(xml).getRootElement());
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) throws MobyException {
		String msg = "<moby:MOBY xmlns:moby=\"http://www.biomoby.org/moby\">\r\n"
				+ "  <moby:mobyContent>\r\n"
				+ "    <moby:mobyData moby:queryID=\"a2_+_s65_+_s165_+_s1290_a2_+_s65_+_s165_+_s1290_+_s1408_a0_+_s3_+_s1409\">\r\n"
				+ "      <moby:Collection moby:articleName=\"alleles\">\r\n"
				+ "        <moby:Simple>\r\n"
				+ "          <Object xmlns=\"http://www.biomoby.org/moby\" namespace=\"DragonDB_Allele\" id=\"def-101\" />\r\n"
				+ "        </moby:Simple>\r\n"
				+ "        <moby:Simple>\r\n"
				+ "          <Object xmlns=\"http://www.biomoby.org/moby\" namespace=\"DragonDB_Allele\" id=\"def-chl\" />\r\n"
				+ "        </moby:Simple>\r\n"
				+ "        <moby:Simple>\r\n"
				+ "          <Object xmlns=\"http://www.biomoby.org/moby\" namespace=\"DragonDB_Allele\" id=\"def-gli\" />\r\n"
				+ "        </moby:Simple>\r\n"
				+ "        <moby:Simple>\r\n"
				+ "          <Object xmlns=\"http://www.biomoby.org/moby\" namespace=\"DragonDB_Allele\" id=\"def-nic\" />\r\n"
				+ "        </moby:Simple>\r\n"
				+ "        <moby:Simple>\r\n"
				+ "          <Object xmlns=\"http://www.biomoby.org/moby\" namespace=\"DragonDB_Allele\" id=\"def-23\" />\r\n"
				+ "        </moby:Simple>\r\n"
				+ "      </moby:Collection>\r\n"
				+ "    </moby:mobyData>\r\n"
				+ "  </moby:mobyContent>\r\n"
				+ "</moby:MOBY>";
		Element inputElement = getDOMDocument(msg).getRootElement();
		String queryID = XMLUtilities.getQueryID(inputElement);
		Element[] simples = XMLUtilities.getSimplesFromCollection(inputElement);

		ArrayList list = new ArrayList();
		for (int j = 0; j < simples.length; j++) {
			Element wrappedSimple = XMLUtilities
					.createMobyDataElementWrapper(simples[j]);
			wrappedSimple = XMLUtilities.renameSimple("Allele", "Object",
					wrappedSimple);
			wrappedSimple = XMLUtilities.setQueryID(wrappedSimple, queryID
					+ "_+_" + XMLUtilities.getQueryID(wrappedSimple));
			list.add(XMLUtilities.extractMobyData(wrappedSimple));
		}
	}

	/*
	 * 
	 * @param current the Element that you would like to search @param name the
	 * name of the element that you would like to find @param list the list to
	 * put the elements that are found in @return a list containing the elements
	 * that are named name
	 */
	private static List listChildren(Element current, String name, List list) {
		if (list == null)
			list = new ArrayList();
		if (current.getName().equals(name))
			list.add(current);
		List children = current.getChildren();
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			if (child instanceof Element)
				listChildren(child, name, list);
		}
		return list;
	}

	/**
	 * 
	 * @param collectionName
	 *            the name you would like the collection to be called
	 * @param simples2makeCollection
	 *            the list of Elements to merge into a collection
	 * @return null if a collection wasnt made, otherwise a fully wrapped
	 *         collection is returned.
	 * @throws MobyException
	 */
	public static Element createCollectionFromListOfSimples(
			String collectionName, List<Element> simples2makeCollection)
			throws MobyException {
		if (simples2makeCollection.size() > 0) {
			// create a collection from the list of
			// simples
			Element mimCollection = new Element("Collection",
					XMLUtilities.MOBY_NS);
			for (Element simple : simples2makeCollection) {
				Element theSimple = XMLUtilities.extractMobyData(simple);
				if (theSimple.getChild("Simple") != null)
					theSimple = theSimple.getChild("Simple");
				else if (theSimple.getChild("Simple", XMLUtilities.MOBY_NS) != null)
					theSimple = theSimple.getChild("Simple",
							XMLUtilities.MOBY_NS);
				mimCollection.addContent(theSimple.detach());
			}
			String mimQueryID = "merged_" + queryCount++;

			mimCollection = XMLUtilities.createMobyDataElementWrapper(
					mimCollection, mimQueryID, null);
			mimCollection = XMLUtilities.renameCollection(collectionName,
					mimCollection);
			mimCollection = XMLUtilities.createMobyDataElementWrapper(
					mimCollection, mimQueryID, null);
			return mimCollection;
		}
		return null;
	}
}
