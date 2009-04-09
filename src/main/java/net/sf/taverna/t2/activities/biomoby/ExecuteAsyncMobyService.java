/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyPrefixResolver;
import org.biomoby.shared.MobyService;
import org.biomoby.shared.parser.MobyTags;
import org.biomoby.w3c.addressing.EndpointReference;
import org.omg.lsae.notifications.AnalysisEvent;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * This class contains one method that is used to execute asynchronous moby
 * services
 * 
 * @author Edward Kawas
 * 
 */

public class ExecuteAsyncMobyService {

	/* async constants */
	private static final String GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION = "http://docs.oasis-open.org/wsrf/rpw-2/GetMultipleResourceProperties/GetMultipleResourcePropertiesRequest";
	private static final String DESTROY_RESOURCE_ACTION = "http://docs.oasis-open.org/wsrf/rlw-2/ImmediateResourceTermination/DestroyRequest";

	private static final String RESOURCE_PROPERTIES_NS = "http://docs.oasis-open.org/wsrf/rp-2";
	private static final String RESOURCE_LIFETIME_NS = "http://docs.oasis-open.org/wsrf/rl-2";

	@SuppressWarnings("unused")
	private static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";
	private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
	private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";
	private static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	private static final String ANON_URI = WSA_NS + "/anonymous";
	private static final String RESULT_PREFIX = "result_";
	private static final String STATUS_PREFIX = "status_";
	
	private static Logger logger = Logger.getLogger(ExecuteAsyncMobyService.class);
	/**
	 * This method does the same as getMultipleResourceProperties, with the
	 * difference that it returns an String instead of a SOAPPart object. The
	 * result is the serialization of the SOAPPart output obtained from
	 * getMultipleResourceProperties.
	 * 
	 * @param msName
	 *            The MOBY service name
	 * @param queryIds
	 *            The array with the queryIds to use. It may contain null
	 *            strings
	 * @param epr
	 *            The EndpointReference object which helds the MOBY asynchronous
	 *            job information
	 * @param asResult
	 *            If this parameter is true, then this call fetches the results
	 *            associated to the input queryIds. If it is false, then this
	 *            call only asks for the job status.
	 * @return When at least one of the strings from queryIds array was not
	 *         null, an String with the serialized answer from the service.
	 *         Otherwise, it returns null.
	 * @throws SOAPException
	 */
	private static String getMultipleResourcePropertiesAsString(String msName,
			String[] queryIds, EndpointReference epr, boolean asResult)
			throws TransformerConfigurationException, SOAPException,
			TransformerException {
		SOAPPart result = getMultipleResourceProperties(msName, queryIds, epr,
				asResult);
		if (result == null)
			return null;
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		DOMSource dombody = new DOMSource(result);

		StringWriter sw = new StringWriter();
		tr.transform(dombody, new StreamResult(sw));

		return sw.toString();
	}

	/**
	 * This method does the check and fetch work related to asynchronous
	 * services. When all the results are fetched, it returns false. When some
	 * recheck must be issued, it returns true.
	 * 
	 * @param msName
	 *            The MOBY service name
	 * @param epr
	 *            The EndpointReference, used for the queries
	 * @param queryIds
	 *            The array which holds the queryIds to ask for. It can contain
	 *            null strings.
	 * @param result
	 *            The array which will hold the mobyData results. This one must
	 *            have the same size as queryIds array.
	 * @return true, if we need more checking iterations. Otherwise, false
	 * @throws MobyException
	 */
	private static boolean checkMobyAsyncJobsStatus(String msName,
			EndpointReference epr, String[] queryIds,
			org.w3c.dom.Element[] result) throws MobyException {
		// Needed to remap results
		HashMap<String, Integer> queryMap = new HashMap<String, Integer>();
		for (int qi = 0; qi < queryIds.length; qi++) {
			String queryId = queryIds[qi];
			if (queryId != null)
				queryMap.put(queryId, new Integer(qi));
		}

		if (queryMap.size() == 0)
			return false;

		try {
			AnalysisEvent[] l_ae = null;
			// First, status from queries
			String response = getMultipleResourcePropertiesAsString(msName,
					queryIds, epr, false);
			if (response != null) {
				l_ae = AnalysisEvent.createFromXML(response);
			}

			if (l_ae == null || l_ae.length == 0) {
				new MobyException(
						"Troubles while checking asynchronous MOBY job status from service "
								+ msName);
			}

			ArrayList<String> finishedQueries = new ArrayList<String>();
			// Second, gather those finished queries
			for (int iae = 0; iae < l_ae.length; iae++) {
				AnalysisEvent ae = l_ae[iae];
				if (ae.isCompleted()) {
					String queryId = ae.getQueryId();
					if (!queryMap.containsKey(queryId)) {
						throw new MobyException(
								"Invalid result queryId on asynchronous MOBY job status fetched from "
										+ msName);
					}
					finishedQueries.add(queryId);
				}
			}

			// Third, let's fetch the results from the finished queries
			if (finishedQueries.size() > 0) {
				String[] resQueryIds = finishedQueries.toArray(new String[0]);
				SOAPPart soapDOM = getMultipleResourceProperties(msName,
						resQueryIds, epr, true);
				NodeList l_mul = soapDOM.getElementsByTagNameNS(
						RESOURCE_PROPERTIES_NS,
						"GetMultipleResourcePropertiesResponse");
				if (l_mul == null || l_mul.getLength() == 0) {
					throw new MobyException(
							"Error while fetching asynchronous MOBY results from "
									+ msName);
				}

				org.w3c.dom.Element mul = (org.w3c.dom.Element) l_mul.item(0);
				for (org.w3c.dom.Node child = mul.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.ELEMENT_NODE
							&& MobyService.BIOMOBY_SERVICE_URI.equals(child
									.getNamespaceURI())) {
						String preQueryId = child.getLocalName();
						int subpos = preQueryId.indexOf(RESULT_PREFIX);
						if (subpos != 0) {
							throw new MobyException(
									"Invalid result prefix on asynchronous MOBY job results fetched from "
											+ msName);
						}
						String queryId = preQueryId.substring(RESULT_PREFIX
								.length());
						if (!queryMap.containsKey(queryId)) {
							throw new MobyException(
									"Invalid result queryId on asynchronous MOBY job results fetched from "
											+ msName);
						}

						org.w3c.dom.Element elchild = (org.w3c.dom.Element) child;
						NodeList l_moby = elchild.getElementsByTagNameNS(
								MobyPrefixResolver.MOBY_XML_NAMESPACE,
								MobyTags.MOBYDATA);
						if (l_moby == null || l_moby.getLength() == 0)
							l_moby = elchild
									.getElementsByTagNameNS(
											MobyPrefixResolver.MOBY_XML_NAMESPACE_INVALID,
											MobyTags.MOBYDATA);

						if (l_moby == null || l_moby.getLength() == 0) {
							throw new MobyException(
									"Recovered empty payload from asynchronous MOBY service "
											+ msName);
						}
						Integer queryPos = queryMap.get(queryId);
						result[queryPos] = (org.w3c.dom.Element) l_moby.item(0);
						// Marking as null
						queryIds[queryPos] = null;
					}
				}
			}

			return finishedQueries.size() != queryMap.size();
		} catch (SOAPException se) {
			throw new MobyException("Error while querying MOBY job status", se);
		} catch (TransformerConfigurationException tce) {
			throw new MobyException(
					"Error while preparing to parse MOBY job status", tce);
		} catch (TransformerException te) {
			throw new MobyException("Error while parsing MOBY job status", te);
		}
	}

	/**
	 * This method calls the input MOBY service using the asynchronous protocol.
	 * 
	 * @param endpoint
	 *            The endpoint of the service.
	 * @param msName
	 *            The MOBY service name.
	 * @param mobyXML
	 *            The MOBY payload to be sent to the service.
	 * @return The MOBY payload with the results from the service.
	 * @throws MobyException
	 */
	public static String executeMobyAsyncService(String endpoint, String msName,
			String mobyXML) throws MobyException {
		// First, let's get the queryIds
		org.w3c.dom.Document message = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();

			message = db.parse(new InputSource(new StringReader(mobyXML)));
		} catch (Throwable t) {
			throw new MobyException("Error while parsing input query", t);
		}

		NodeList l_data = message.getElementsByTagNameNS(
				MobyPrefixResolver.MOBY_XML_NAMESPACE, MobyTags.MOBYDATA);
		if (l_data == null || l_data.getLength() == 0) {
			l_data = message.getElementsByTagNameNS(
					MobyPrefixResolver.MOBY_XML_NAMESPACE_INVALID,
					MobyTags.MOBYDATA);
		}

		// Freeing resources
		message = null;

		if (l_data == null || l_data.getLength() == 0) {
			throw new MobyException("Empty asynchronous MOBY query!");
		}

		int nnode = l_data.getLength();
		String[] queryIds = new String[nnode];
		String[] tmpQueryIds = new String[nnode];
		org.w3c.dom.Element[] results = new org.w3c.dom.Element[nnode];
		for (int inode = 0; inode < nnode; inode++) {
			String queryId = null;

			org.w3c.dom.Element mdata = (org.w3c.dom.Element) l_data
					.item(inode);

			queryId = mdata.getAttribute(MobyTags.QUERYID);
			if (queryId == null || queryId.length() == 0)
				queryId = mdata
						.getAttributeNS(MobyPrefixResolver.MOBY_XML_NAMESPACE,
								MobyTags.QUERYID);
			if (queryId == null || queryId.length() == 0)
				queryId = mdata.getAttributeNS(
						MobyPrefixResolver.MOBY_XML_NAMESPACE_INVALID,
						MobyTags.QUERYID);

			if (queryId == null || queryId.length() == 0) {
				throw new MobyException(
						"Unable to extract queryId for outgoing MOBY message");
			}

			tmpQueryIds[inode] = queryIds[inode] = queryId;
			results[inode] = null;
		}

		// Freeing resources
		l_data = null;

		// Second, let's launch
		EndpointReference epr = launchMobyAsyncService(endpoint, msName,
				mobyXML);

		// Third, waiting for the results
		try {
			// FIXME - add appropriate values here
			long pollingInterval = 1000L; // proc.getRetryDelay();
			double backoff = 1.0;// proc.getBackoff();

			// Max: one minute pollings
			long maxPollingInterval = 60000L;

			// Min: one second
			if (pollingInterval <= 0L)
				pollingInterval = 1000L;

			// Backoff: must be bigger than 1.0
			if (backoff <= 1.0)
				backoff = 1.5;

			do {
				try {
					Thread.sleep(pollingInterval);
				} catch (InterruptedException ie) {
					// DoNothing(R)
				}

				if (pollingInterval != maxPollingInterval) {
					pollingInterval = (long) ((double) pollingInterval * backoff/*
																				 * proc.
																				 * getBackoff
																				 * (
																				 * )
																				 */);
					if (pollingInterval > maxPollingInterval) {
						pollingInterval = maxPollingInterval;
					}
				}
			} while (checkMobyAsyncJobsStatus(msName, epr, tmpQueryIds, results));
		} finally {
			try {
				freeAsyncResources(msName, epr);
			} catch (SOAPException se) {
				logger.info(
						"An error was fired while freeing MOBY asynchronous resources from "
								+ msName, se);
			}
		}

		// Fourth, assembling back the results
		org.w3c.dom.Document resdoc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			resdoc = db.newDocument();

			org.w3c.dom.Element mobyroot = resdoc.createElementNS(
					MobyPrefixResolver.MOBY_XML_NAMESPACE, MobyTags.MOBY);
			resdoc.appendChild(mobyroot);
			org.w3c.dom.Element mobycontent = resdoc
					.createElementNS(MobyPrefixResolver.MOBY_XML_NAMESPACE,
							MobyTags.MOBYCONTENT);
			mobyroot.appendChild(mobycontent);

			// Results array already contains mobyData

			for (org.w3c.dom.Element result : results) {
				mobycontent.appendChild(resdoc.importNode(result, true));
			}
		} catch (Throwable t) {
			throw new MobyException("Error while assembling output", t);
		}

		// Fifth, returning results
		try {
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			DOMSource dombody = new DOMSource(resdoc);

			StringWriter sw = new StringWriter();
			tr.transform(dombody, new StreamResult(sw));

			return sw.toString();
		} catch (Throwable t) {
			throw new MobyException("Error while assembling output", t);
		}
	}
	
	/**
	 * This method free the asynchronous MOBY resources associated to the job
	 * identifier tied to the EndpointReference object passed as input.
	 * 
	 * @param msName
	 *            The MOBY service name
	 * @param epr
	 *            The EndpointReference object which holds the MOBY asynchronous
	 *            job information
	 * @throws SOAPException
	 */
	private static void freeAsyncResources(String msName, EndpointReference epr)
			throws SOAPException {
		Service service = Service.create(new QName(
				MobyService.BIOMOBY_SERVICE_URI, msName + "Service"));
		QName mQName = new QName(MobyService.BIOMOBY_SERVICE_URI,
				"WSRF_Operations_Port");
		service.addPort(mQName, SOAPBinding.SOAP11HTTP_BINDING, epr
				.getAddress());

		Dispatch<SOAPMessage> dispatch = service.createDispatch(mQName,
				SOAPMessage.class, Service.Mode.MESSAGE);
		Map<String, Object> rc = dispatch.getRequestContext();
		rc.put(BindingProvider.SOAPACTION_USE_PROPERTY, new Boolean(true));
		rc.put(BindingProvider.SOAPACTION_URI_PROPERTY,
				GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION);

		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage request = mf.createMessage();
		SOAPPart part = request.getSOAPPart();

		String mobyPrefix = "mobyws";
		String wsaPrefix = "wsa";
		String wsuPrefix = "wsu";
		// Obtain the SOAPEnvelope and header and body elements.
		SOAPEnvelope env = part.getEnvelope();
		SOAPHeader header = env.getHeader();
		SOAPBody body = env.getBody();

		header.addNamespaceDeclaration(mobyPrefix,
				MobyService.BIOMOBY_SERVICE_URI);
		header.addNamespaceDeclaration(wsaPrefix, WSA_NS);
		header.addNamespaceDeclaration(wsuPrefix, WSU_NS);
		// This is for the action
		SOAPElement actionRoot = header.addChildElement("Action", wsaPrefix,
				WSA_NS);
		actionRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS),
				"Action");
		actionRoot.addTextNode(DESTROY_RESOURCE_ACTION);

		// This is for the To
		SOAPElement toRoot = header.addChildElement("To", wsaPrefix, WSA_NS);
		toRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS), "To");
		toRoot.addTextNode(epr.getAddress());

		// And this is for the mobyws
		SOAPElement mobywsRoot = header.addChildElement("ServiceInvocationId",
				mobyPrefix, MobyService.BIOMOBY_SERVICE_URI);
		mobywsRoot.addNamespaceDeclaration(wsaPrefix, WSA_NS);
		mobywsRoot.addAttribute(env.createName("isReferenceParameter",
				wsaPrefix, WSA_NS), "true");
		mobywsRoot.addTextNode(epr.getServiceInvocationId());

		// At last, the replyto
		SOAPElement replyRoot = header.addChildElement("ReplyTo", wsaPrefix,
				WSA_NS);
		replyRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS),
				"ReplyTo");
		SOAPElement addr = replyRoot.addChildElement("Address", wsaPrefix,
				WSA_NS);
		addr.addTextNode(ANON_URI);

		// Let's disable the headers
		// ((WSBindingProvider)dispatch).setOutboundHeaders(headers);

		// Now the SOAP body
		body.addChildElement("Destroy", "rl", RESOURCE_LIFETIME_NS);

		request.saveChanges();
		// We don't mind what it is returned
		dispatch.invoke(request);
	}
	
	/**
	 * This method is used to launch an asynchronous MOBY job.
	 * 
	 * @param endpoint
	 *            The endpoint of the service.
	 * @param msName
	 *            The MOBY service name.
	 * @param mobyXML
	 *            The MOBY payload to be sent to the service.
	 * @return The EndpointReference object which helds the details of the MOBY
	 *         asynchronous job.
	 * @throws MobyException
	 */
	private static EndpointReference launchMobyAsyncService(String endpoint,
			String msName, String mobyXML) throws MobyException {
		try {
			Service service = Service.create(new QName(
					MobyService.BIOMOBY_SERVICE_URI, msName + "Service"));
			QName mQName = new QName(MobyService.BIOMOBY_SERVICE_URI, msName
					+ "Port");

			service.addPort(mQName, SOAPBinding.SOAP11HTTP_BINDING, endpoint);
			Dispatch<SOAPMessage> dispatch = service.createDispatch(mQName,
					SOAPMessage.class, Service.Mode.MESSAGE);
			Map<String, Object> rc = dispatch.getRequestContext();
			rc.put(BindingProvider.SOAPACTION_USE_PROPERTY, new Boolean(true));
			rc.put(BindingProvider.SOAPACTION_URI_PROPERTY,
					MobyService.BIOMOBY_SERVICE_URI + "#" + msName + "_submit");

			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage request = mf.createMessage();
			SOAPPart part = request.getSOAPPart();

			String mobyPrefix = "mobyws";
			String xsiPrefix = "xsi";

			// Obtain the SOAPEnvelope and header and body elements.
			SOAPEnvelope env = part.getEnvelope();
			SOAPBody body = env.getBody();

			// Now the SOAP body
			body.addNamespaceDeclaration(mobyPrefix,
					MobyService.BIOMOBY_SERVICE_URI);
			SOAPElement rootMessage = body.addChildElement(msName + "_submit",
					mobyPrefix, MobyService.BIOMOBY_SERVICE_URI);
			SOAPElement data = rootMessage.addChildElement("data", mobyPrefix,
					MobyService.BIOMOBY_SERVICE_URI);
			data.addNamespaceDeclaration(xsiPrefix,
					MobyPrefixResolver.XSI_NAMESPACE2001);
			data.addNamespaceDeclaration("xsd", XSD_NS);
			data.addAttribute(env.createName("type", xsiPrefix,
					MobyPrefixResolver.XSI_NAMESPACE2001), "xsd:string");
			data.addTextNode(mobyXML);

			request.saveChanges();
			SOAPMessage outputMessage = dispatch.invoke(request);
			DOMSource output = new DOMSource(outputMessage.getSOAPPart()
					.getEnvelope());

			StringWriter sw = new StringWriter();
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			tr.transform(output, new StreamResult(sw));

			String response = sw.toString();
			return EndpointReference.createFromXML(response);
		} catch (SOAPException pce) {
			throw new MobyException(
					"Unable to create SOAP document builder for MOBY asynchronous call submission",
					pce);
		} catch (TransformerConfigurationException tce) {
			throw new MobyException(
					"Unable to create transformer factory for MOBY asynchronous call response",
					tce);
		} catch (TransformerException te) {
			throw new MobyException(
					"Unable to create transformer for MOBY asynchronous call response",
					te);
		}
	}

	

	/**
	 * This method issues WSRF getMultipleResourceProperties calls. As this call
	 * is used in BioMOBY for polling and for result fetching, it has an
	 * additional parameter which handles the call mode.
	 * 
	 * @param msName
	 *            The MOBY service name
	 * @param queryIds
	 *            The array with the queryIds to use. It may contain null
	 *            strings
	 * @param epr
	 *            The EndpointReference object which helds the MOBY asynchronous
	 *            job information
	 * @param asResult
	 *            If this parameter is true, then this call fetches the results
	 *            associated to the input queryIds. If it is false, then this
	 *            call only asks for the job status.
	 * @return When at least one of the strings from queryIds array was not
	 *         null, a SOAPPart object is returned with the answer for the
	 *         request issued to the MOBY service. Otherwise, it returns null.
	 * @throws SOAPException
	 */
	private static SOAPPart getMultipleResourceProperties(String msName,
			String[] queryIds, EndpointReference epr, boolean asResult)
			throws SOAPException {
		String op = asResult ? RESULT_PREFIX : STATUS_PREFIX;

		Service service = Service.create(new QName(
				MobyService.BIOMOBY_SERVICE_URI, msName + "Service"));
		QName mQName = new QName(MobyService.BIOMOBY_SERVICE_URI,
				"WSRF_Operations_Port");
		service.addPort(mQName, SOAPBinding.SOAP11HTTP_BINDING, epr
				.getAddress());

		Dispatch<SOAPMessage> dispatch = service.createDispatch(mQName,
				SOAPMessage.class, Service.Mode.MESSAGE);
		Map<String, Object> rc = dispatch.getRequestContext();
		rc.put(BindingProvider.SOAPACTION_USE_PROPERTY, new Boolean(true));
		rc.put(BindingProvider.SOAPACTION_URI_PROPERTY,
				GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION);

		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage request = mf.createMessage();
		SOAPPart part = request.getSOAPPart();

		String mobyPrefix = "mobyws";
		String wsaPrefix = "wsa";
		String wsuPrefix = "wsu";
		// Obtain the SOAPEnvelope and header and body elements.
		SOAPEnvelope env = part.getEnvelope();
		SOAPHeader header = env.getHeader();
		SOAPBody body = env.getBody();

		header.addNamespaceDeclaration(mobyPrefix,
				MobyService.BIOMOBY_SERVICE_URI);
		header.addNamespaceDeclaration(wsaPrefix, WSA_NS);
		header.addNamespaceDeclaration(wsuPrefix, WSU_NS);
		// This is for the action
		SOAPElement actionRoot = header.addChildElement("Action", wsaPrefix,
				WSA_NS);
		actionRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS),
				"Action");
		actionRoot.addTextNode(GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION);

		// This is for the To
		SOAPElement toRoot = header.addChildElement("To", wsaPrefix, WSA_NS);
		toRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS), "To");
		toRoot.addTextNode(epr.getAddress());

		// And this is for the mobyws
		SOAPElement mobywsRoot = header.addChildElement("ServiceInvocationId",
				mobyPrefix, MobyService.BIOMOBY_SERVICE_URI);
		mobywsRoot.addNamespaceDeclaration(wsaPrefix, WSA_NS);
		mobywsRoot.addAttribute(env.createName("isReferenceParameter",
				wsaPrefix, WSA_NS), "true");
		mobywsRoot.addTextNode(epr.getServiceInvocationId());

		// At last, the replyto
		SOAPElement replyRoot = header.addChildElement("ReplyTo", wsaPrefix,
				WSA_NS);
		replyRoot.addAttribute(env.createName("Id", wsuPrefix, WSU_NS),
				"ReplyTo");
		SOAPElement addr = replyRoot.addChildElement("Address", wsaPrefix,
				WSA_NS);
		addr.addTextNode(ANON_URI);

		// Let's disable the headers
		// ((WSBindingProvider)dispatch).setOutboundHeaders(headers);

		// Now the SOAP body
		SOAPElement smrp = body.addChildElement(
				"GetMultipleResourceProperties", "rp", RESOURCE_PROPERTIES_NS);
		boolean doSubmit = false;
		for (String queryId : queryIds) {
			if (queryId != null) {
				doSubmit = true;
				SOAPElement sii = smrp.addChildElement("ResourceProperty",
						"rp", RESOURCE_PROPERTIES_NS);
				sii.addNamespaceDeclaration(mobyPrefix,
						MobyService.BIOMOBY_SERVICE_URI);
				sii.addTextNode(mobyPrefix + ":" + op + queryId);
			}
		}

		if (doSubmit) {
			request.saveChanges();
			SOAPMessage output = dispatch.invoke(request);

			return output.getSOAPPart();
		} else {
			return null;
		}
	}

}
