/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyPrefixResolver;
import org.biomoby.shared.parser.MobyTags;
import org.biomoby.w3c.addressing.EndpointReference;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.omg.lsae.notifications.AnalysisEvent;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class contains one method that is used to execute asynchronous HTTP POST
 * services
 * 
 * @author Edward Kawas
 * 
 */
public class ExecuteAsyncCgiService {

	private static final String GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION = "http://docs.oasis-open.org/wsrf/rpw-2/GetMultipleResourceProperties/GetMultipleResourcePropertiesRequest";
	private static final String DESTROY_RESOURCE_ACTION = "http://docs.oasis-open.org/wsrf/rlw-2/ImmediateResourceTermination/DestroyRequest";

	private static final String RESOURCE_PROPERTIES_NS = "http://docs.oasis-open.org/wsrf/rp-2";
	private static final String RESULT_PREFIX = "result_";
	private static final String STATUS_PREFIX = "status_";
	private static Logger logger = Logger
			.getLogger(ExecuteAsyncCgiService.class);

	/**
	 * 
	 * @param url
	 * @param serviceName
	 * @param xml
	 * @return
	 */
	public static String executeMobyCgiAsyncService(String url,
			String serviceName, String xml) throws MobyException {

		// First, let's get the queryIds
		org.w3c.dom.Document message = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();

			message = db.parse(new InputSource(new StringReader(xml)));
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
		String[] results = new String[nnode];
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
		EndpointReference epr = launchCgiAsyncService(url, xml);

		// Third, waiting for the results
		try {
			// FIXME - add appropriate values here
			long pollingInterval = 1000L;
			double backoff = 1.0;

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
					pollingInterval = (long) ((double) pollingInterval * backoff);
					if (pollingInterval > maxPollingInterval) {
						pollingInterval = maxPollingInterval;
					}
				}
			} while (pollAsyncCgiService(serviceName, url, epr, tmpQueryIds,
					results));
		} finally {

			// Call destroy on this service ....
			freeCgiAsyncResources(url, epr);

		}

		// Fourth, assembling back the results

		// Results array already contains mobyData
		Element[] mobydatas = new Element[results.length];
		for (int x = 0; x < results.length; x++) {
			// TODO remove the extra wrapping from our result
			try {
				Element inputElement = XMLUtilities.getDOMDocument(results[x])
						.getRootElement();
				if (inputElement.getName().indexOf(
						"GetMultipleResourcePropertiesResponse") >= 0)
					if (inputElement.getChildren().size() > 0)
						inputElement = (Element) inputElement.getChildren()
								.get(0);
				if (inputElement.getName().indexOf("result_") >= 0)
					if (inputElement.getChildren().size() > 0)
						inputElement = (Element) inputElement.getChildren()
								.get(0);
				// replace results[x]
				mobydatas[x] = inputElement;
			} catch (MobyException e) {
				// TODO what should i do?
			}
		}
		Element e = null;
		try {
			e = XMLUtilities.createMultipleInvokations(mobydatas);
		} catch (Exception ex) {
			logger
					.error("There was a problem creating our XML message ...",
							ex);
		}
		// Fifth, returning results
		return e == null ? "" : new XMLOutputter(Format.getPrettyFormat())
				.outputString(e);
	}

	private static void freeCgiAsyncResources(String endpoint,
			EndpointReference epr) throws MobyException {
		// construct the Httpclient
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "jMoby/Taverna2");
		// create the post method
		PostMethod method = new PostMethod(endpoint + "/destroy");

		// put our data in the request
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(
					"<Destroy xmlns=\"http://docs.oasis-open.org/wsrf/rl-2\"/>",
					"text/xml", null);
		} catch (UnsupportedEncodingException e) {
			throw new MobyException("Problem posting data to webservice", e);
		}
		method.setRequestEntity(entity);

		// set the header
		StringBuffer httpheader = new StringBuffer();
		httpheader.append("<moby-wsrf>");
		httpheader
				.append("<wsa:Action xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
						+ DESTROY_RESOURCE_ACTION + "</wsa:Action>");
		httpheader
				.append("<wsa:To xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"To\">"
						+ endpoint + "</wsa:To>");
		httpheader
				.append("<mobyws:ServiceInvocationId xmlns:mobyws=\"http://biomoby.org/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsa:IsReferenceParameter=\"true\">"
						+ epr.getServiceInvocationId()
						+ "</mobyws:ServiceInvocationId>");
		httpheader.append("</moby-wsrf>");
		method.addRequestHeader("moby-wsrf", httpheader.toString().replaceAll(
				"\r\n", ""));
		// retry up to 10 times
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(10, true));

		// call the method
		try {
			int result = client.executeMethod(method);
			if (result != HttpStatus.SC_OK)
				throw new MobyException(
						"Async HTTP POST service returned code: " + result
								+ "\n" + method.getStatusLine());
		} catch (IOException e) {
			throw new MobyException("Problem reading response from webservice",
					e);
		} finally {
			// Release current connection to the connection pool once you are
			// done
			method.releaseConnection();
		}
	}

	/**
	 * 
	 * @param endpoint
	 *            the url to the service to call
	 * @param xml
	 *            the BioMOBY input message
	 * @return EndpointReference the EPR returned by the service
	 * @throws MobyException
	 */
	private static EndpointReference launchCgiAsyncService(String endpoint,
			String xml) throws MobyException {
		// construct the Httpclient
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "jMoby/Taverna2");
		// create the post method
		PostMethod method = new PostMethod(endpoint);

		// put our data in the request
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(xml, "text/xml", null);
		} catch (UnsupportedEncodingException e) {
			throw new MobyException("Problem posting data to webservice", e);
		}
		method.setRequestEntity(entity);

		// retry up to 10 times
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(10, true));

		// call the method
		try {
			int result = client.executeMethod(method);
			if (result != HttpStatus.SC_OK)
				throw new MobyException(
						"Async HTTP POST service returned code: " + result
								+ "\n" + method.getStatusLine());
			return EndpointReference.createFromXML(method.getResponseHeader(
					"moby-wsrf").getValue());
		} catch (IOException e) {
			throw new MobyException("Problem reading response from webservice",
					e);
		} finally {
			// Release current connection to the connection pool once you are
			// done
			method.releaseConnection();
		}
	}

	private static boolean pollAsyncCgiService(String msName, String url,
			EndpointReference epr, String[] queryIds, String[] result)
			throws MobyException {
		// Needed to remap results
		HashMap<String, Integer> queryMap = new HashMap<String, Integer>();
		for (int qi = 0; qi < queryIds.length; qi++) {
			String queryId = queryIds[qi];
			if (queryId != null)
				queryMap.put(queryId, new Integer(qi));
		}

		if (queryMap.size() == 0)
			return false;

		// construct the GetMultipleResourceProperties XML
		StringBuffer xml = new StringBuffer();
		xml.append("<wsrf-rp:GetMultipleResourceProperties xmlns:wsrf-rp='"
				+ RESOURCE_PROPERTIES_NS
				+ "' xmlns:mobyws='http://biomoby.org/'>");
		for (String q : queryMap.keySet())
			xml.append("<wsrf-rp:ResourceProperty>mobyws:" + STATUS_PREFIX + q
					+ "</wsrf-rp:ResourceProperty>");
		xml.append("</wsrf-rp:GetMultipleResourceProperties>");

		StringBuffer httpheader = new StringBuffer();
		httpheader.append("<moby-wsrf>");
		httpheader
				.append("<wsa:Action xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
						+ GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION
						+ "</wsa:Action>");
		httpheader
				.append("<wsa:To xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"To\">"
						+ url + "</wsa:To>");
		httpheader
				.append("<mobyws:ServiceInvocationId xmlns:mobyws=\"http://biomoby.org/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsa:IsReferenceParameter=\"true\">"
						+ epr.getServiceInvocationId()
						+ "</mobyws:ServiceInvocationId>");
		httpheader.append("</moby-wsrf>");

		AnalysisEvent[] l_ae = null;
		// First, status from queries
		String response = "";
		// construct the Httpclient
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "jMoby/Taverna2");
		// create the post method
		PostMethod method = new PostMethod(url + "/status");
		// add the moby-wsrf header (with no newlines)
		method.addRequestHeader("moby-wsrf", httpheader.toString().replaceAll(
				"\r\n", ""));

		// put our data in the request
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(xml.toString(), "text/xml", null);
		} catch (UnsupportedEncodingException e) {
			throw new MobyException("Problem posting data to webservice", e);
		}
		method.setRequestEntity(entity);

		// retry up to 10 times
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(10, true));

		// call the method
		try {
			if (client.executeMethod(method) != HttpStatus.SC_OK)
				throw new MobyException(
						"Async HTTP POST service returned code: "
								+ method.getStatusCode() + "\n"
								+ method.getStatusLine()
								+ "\nduring our polling request");
			response = stream2String(method.getResponseBodyAsStream());
		} catch (IOException e) {
			throw new MobyException("Problem reading response from webservice",
					e);
		} finally {
			// Release current connection to the connection pool once you
			// are
			// done
			method.releaseConnection();
		}

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
			for (int x = 0; x < resQueryIds.length; x++) {
				// construct the GetMultipleResourceProperties XML
				xml = new StringBuffer();
				xml
						.append("<wsrf-rp:GetMultipleResourceProperties xmlns:wsrf-rp='"
								+ RESOURCE_PROPERTIES_NS
								+ "' xmlns:mobyws='http://biomoby.org/'>");
				for (String q : resQueryIds)
					xml.append("<wsrf-rp:ResourceProperty>mobyws:" + RESULT_PREFIX + q
							+ "</wsrf-rp:ResourceProperty>");
				xml.append("</wsrf-rp:GetMultipleResourceProperties>");

				httpheader = new StringBuffer();
				httpheader.append("<moby-wsrf>");
				httpheader
						.append("<wsa:Action xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
								+ GET_MULTIPLE_RESOURCE_PROPERTIES_ACTION
								+ "</wsa:Action>");
				httpheader
						.append("<wsa:To xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"To\">"
								+ url + "</wsa:To>");
				httpheader
						.append("<mobyws:ServiceInvocationId xmlns:mobyws=\"http://biomoby.org/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" wsa:IsReferenceParameter=\"true\">"
								+ epr.getServiceInvocationId()
								+ "</mobyws:ServiceInvocationId>");
				httpheader.append("</moby-wsrf>");
				client = new HttpClient();
				client.getParams().setParameter("http.useragent",
						"jMoby/Taverna2");
				// create the post method
				method = new PostMethod(url + "/results");
				// add the moby-wsrf header (with no newlines)
				method.addRequestHeader("moby-wsrf", httpheader.toString()
						.replaceAll("\r\n", ""));

				// put our data in the request
				entity = null;
				try {
					entity = new StringRequestEntity(xml.toString(),
							"text/xml", null);
				} catch (UnsupportedEncodingException e) {
					throw new MobyException(
							"Problem posting data to webservice", e);
				}
				method.setRequestEntity(entity);

				// retry up to 10 times
				client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(10, true));

				// call the method
				try {
					if (client.executeMethod(method) != HttpStatus.SC_OK)
						throw new MobyException(
								"Async HTTP POST service returned code: "
										+ method.getStatusCode() + "\n"
										+ method.getStatusLine()
										+ "\nduring our polling request");
					// place the result in the array
					result[x] = stream2String(method.getResponseBodyAsStream());
					// Marking as null
					queryIds[x] = null;
				} catch (IOException e) {
					logger.warn("Problem getting result from webservice\n"
							+ e.getMessage());
				} finally {
					// Release current connection
					method.releaseConnection();
				}
			}

		}
		return finishedQueries.size() != queryMap.size();
	}

	private static String stream2String(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		String newline = System.getProperty("line.separator");
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + newline);
			}
		} catch (IOException e) {
			logger.warn("Exception reading input stream ...", e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.warn("Exception closing input stream ...", e);
			}
		}
		return sb.toString();
	}
}
