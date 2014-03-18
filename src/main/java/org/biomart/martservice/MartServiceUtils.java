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
 * Filename           $RCSfile: MartServiceUtils.java,v $
 * Revision           $Revision: 1.12 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008/07/31 15:06:49 $
 *               by   $Author: davidwithers $
 * Created on 17-Mar-2006
 *****************************************************************/
package org.biomart.martservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Dataset;
import org.biomart.martservice.query.Query;
import org.biomart.martservice.query.QueryXMLHandler;
import org.ensembl.mart.lib.config.ConfigurationException;
import org.ensembl.mart.lib.config.DatasetConfig;
import org.ensembl.mart.lib.config.DatasetConfigXMLUtils;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 * Utility class for interacting with a BioMart web service.
 * 
 * @author David Withers
 */
public class MartServiceUtils {
	private static Logger logger = Logger.getLogger(MartServiceUtils.class);

	private static String lineSeparator = System.getProperty("line.separator");

	public static final String QUERY_ATTRIBUTE = "query";

	public static final String DATASET_ATTRIBUTE = "dataset";

	public static final String MART_ATTRIBUTE = "mart";

	public static final String SCHEMA_ATTRIBUTE = "virtualschema";

	public static final String TYPE_ATTRIBUTE = "type";

	public static final String MART_USER_ATTRIBUTE = "martuser";

	public static final String INTERFACE_ATTRIBUTE = "interface";

	public static final String REQUEST_ID_ATTRIBUTE = "requestid";

	public static final String REGISTRY_VALUE = "registry";

	public static final String VERSION_VALUE = "version";

	public static final String DATASETS_VALUE = "datasets";

	public static final String DATASET_VALUE = "dataset";

	public static final String CONFIGURATION_VALUE = "configuration";

	/**
	 * Sends a registry request to the Biomart webservice and constructs a
	 * MartRegistry from the XML returned by the webservice.
	 * 
	 * @param martServiceLocation
	 *            the URL of the Biomart webservice
	 * @return a MartRegistry
	 * @throws MartServiceException
	 *             if the Biomart webservice returns an error or is unavailable
	 */
	public static MartRegistry getRegistry(String martServiceLocation,
			String requestId) throws MartServiceException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new NameValuePair(TYPE_ATTRIBUTE, REGISTRY_VALUE));
		if (requestId != null) {
			data.add(new NameValuePair(REQUEST_ID_ATTRIBUTE, requestId));
		}
		HttpMethod method = new GetMethod(martServiceLocation);
		method.setQueryString(data
				.toArray(new NameValuePair[data.size()]));
		try {
			InputStream in = executeMethod(method, martServiceLocation);
			Document document = new SAXBuilder().build(in);
			Element root = document.getRootElement();
			return MartServiceXMLHandler.elementToRegistry(root,
					Namespace.NO_NAMESPACE);
		} catch (IOException e) {
			String errorMessage = "Error getting registry from "
					+ martServiceLocation;
			throw new MartServiceException(errorMessage, e);
		} catch (JDOMException e) {
			String errorMessage = "Error getting registry from "
					+ martServiceLocation;
			throw new MartServiceException(errorMessage, e);
		} finally {
			method.releaseConnection();
		}

	}

	public static String getVersion(String martServiceLocation,
			String requestId, MartURLLocation mart) throws MartServiceException {
		String errorMessage = "Error getting version from " + martServiceLocation;

		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new NameValuePair(TYPE_ATTRIBUTE, VERSION_VALUE));
		if (mart.getVirtualSchema() != null) {
			data.add(new NameValuePair(SCHEMA_ATTRIBUTE, mart
					.getVirtualSchema()));
		}
		data.add(new NameValuePair(MART_ATTRIBUTE, mart.getName()));
		if (requestId != null) {
			data.add(new NameValuePair(REQUEST_ID_ATTRIBUTE, requestId));
		}
		if (requestId != null) {
			data.add(new NameValuePair(REQUEST_ID_ATTRIBUTE, requestId));
		}
		HttpMethod method = new GetMethod(martServiceLocation);
		method.setQueryString(data
				.toArray(new NameValuePair[data.size()]));
		try {
			InputStream in = executeMethod(method, martServiceLocation);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			String version = bufferedReader.readLine();
			if (version == null) {
				throw new MartServiceException(errorMessage + ": No version returned");
			}
			version = version.trim();
			// fix for biomart's 'let's add a blank line' thing
			if ("".equals(version)) {
				version = bufferedReader.readLine();
				if (version == null) {
					throw new MartServiceException(errorMessage + ": No version returned");
				}
				version = version.trim();
			}
			bufferedReader.close();
			return version;
		} catch (IOException e) {
			throw new MartServiceException(errorMessage, e);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Sends a datasets request to the Biomart webservice and constructs an
	 * array of MartDataset from the tab separated rows of data returned by the
	 * webservice.
	 * 
	 * @param martServiceLocation
	 *            the URL of the Biomart webservice
	 * @param mart
	 *            the mart to get datasets from
	 * @return an array of MartDataset
	 * @throws MartServiceException
	 *             if the Biomart webservice returns an error or is unavailable
	 */
	public static MartDataset[] getDatasets(String martServiceLocation,
			String requestId, MartURLLocation mart) throws MartServiceException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new NameValuePair(TYPE_ATTRIBUTE, DATASETS_VALUE));
		if (mart.getVirtualSchema() != null) {
			data.add(new NameValuePair(SCHEMA_ATTRIBUTE, mart
					.getVirtualSchema()));
		}
		data.add(new NameValuePair(MART_ATTRIBUTE, mart.getName()));
		if (mart.getMartUser() != null) {
			data
					.add(new NameValuePair(MART_USER_ATTRIBUTE, mart
							.getMartUser()));
		}
		if (requestId != null) {
			data.add(new NameValuePair(REQUEST_ID_ATTRIBUTE, requestId));
		}
		HttpMethod method = new GetMethod(martServiceLocation);
		method.setQueryString(data
				.toArray(new NameValuePair[data.size()]));
		try {
			InputStream in = executeMethod(method, martServiceLocation);

			MartDataset[] datasets = tabSeparatedReaderToDatasets(
					new InputStreamReader(in), mart);
			in.close();
			return datasets;
		} catch (IOException e) {
			String errorMessage = "Error getting datasets from "
					+ martServiceLocation;
			throw new MartServiceException(errorMessage, e);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Sends a configuration request to the Biomart webservice and constructs a
	 * DatasetConfig from the XML returned by the webservice.
	 * 
	 * @param martServiceLocation
	 *            the URL of the Biomart webservice
	 * @param dataset
	 *            the dataset to get the configuration for
	 * @return a DatasetConfig
	 * @throws MartServiceException
	 *             if the Biomart webservice returns an error or is unavailable
	 */
	public static DatasetConfig getDatasetConfig(String martServiceLocation,
			String requestId, MartDataset dataset) throws MartServiceException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new NameValuePair(TYPE_ATTRIBUTE, CONFIGURATION_VALUE));
		MartURLLocation mart = dataset.getMartURLLocation();
		// if the dataset has a location specify the virtual schema to uniquely
		// identify the dataset
		if (mart != null && mart.getVirtualSchema() != null) {
			data.add(new NameValuePair(SCHEMA_ATTRIBUTE, mart
					.getVirtualSchema()));
		}
		data.add(new NameValuePair(DATASET_VALUE, dataset.getName()));
//		if (dataset.getInterface() != null) {
//			data.add(new NameValuePair(INTERFACE_ATTRIBUTE, dataset
//					.getInterface()));
//		}
		if (mart != null && mart.getMartUser() != null) {
			data
					.add(new NameValuePair(MART_USER_ATTRIBUTE, mart
							.getMartUser()));
		}
		if (requestId != null) {
			data.add(new NameValuePair(REQUEST_ID_ATTRIBUTE, requestId));
		}
		HttpMethod method = new GetMethod(martServiceLocation);
		method.setQueryString(data
				.toArray(new NameValuePair[data.size()]));

		try {
			InputStream in = executeMethod(method, martServiceLocation);

			DatasetConfigXMLUtils datasetConfigXMLUtils = new DatasetConfigXMLUtils(
					true);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new InputSource(in));
			// Document doc = datasetConfigXMLUtils.getDocumentForXMLStream(in);

			DatasetConfig datasetConfig = datasetConfigXMLUtils
					.getDatasetConfigForDocument(doc);
			datasetConfigXMLUtils.loadDatasetConfigWithDocument(datasetConfig,
					doc);
			return datasetConfig;
		} catch (ConfigurationException e) {
			String errorMessage = "Error parsing configuration from "
					+ martServiceLocation;
			logger.debug(errorMessage, e);
			throw new MartServiceException(errorMessage, e);
		} catch (JDOMException e) {
			String errorMessage = "Error parsing configuration from "
					+ martServiceLocation;
			logger.debug(errorMessage, e);
			throw new MartServiceException(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Error getting configuration from "
					+ martServiceLocation;
			logger.debug(errorMessage, e);
			throw new MartServiceException(errorMessage, e);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Sends a query to the Biomart webservice and constructs an array of List
	 * of String results from the tab separated rows of data returned by the
	 * webservice.
	 * 
	 * @param martServiceLocation
	 *            the URL of the Biomart webservice
	 * @param query
	 *            the query to send to the webservice
	 * @return an array of List of String
	 * @throws MartServiceException
	 *             if the Biomart webservice returns an error or is unavailable
	 */
	public static Object[] getResults(String martServiceLocation,
			String requestId, Query query) throws MartServiceException {
		Object[] results = new Object[0];
		// int attributes = query.getAttributes().size();
		int attributes = getAttributeCount(query.getAttributes());
		boolean count = query.getCount() == 1;
		// if there are no attributes and we're not doing a count there's no
		// point in doing the query
		if (attributes > 0 || count) {
			// The 'new' 0.5 server now resolves the attribute lists so there's
			// no need to do the split here any more
			// String queryXml = queryToXML(splitAttributeLists(query));
			String queryXml = queryToXML(query);
			logger.info(queryXml);
			NameValuePair[] data = { new NameValuePair(QUERY_ATTRIBUTE,
					queryXml) };
			PostMethod method = new PostMethod(martServiceLocation);
			method.setRequestBody(data);

			try {
				InputStream in = executeMethod(method, martServiceLocation);
				if (query.getFormatter() == null) {
					results = tabSeparatedReaderToResults(
							new InputStreamReader(in), count ? 1 : attributes);
					if (!count) {
						results = reassembleAttributeLists(results, query);
					}
				} else {
					results = readResult(in, query.getFormatter());
				}
				in.close();
			} catch (IOException e) {
				String errorMessage = "Error reading data from "
						+ martServiceLocation;
				throw new MartServiceException(errorMessage, e);
			} finally {
				method.releaseConnection();
			}

		}

		return results;
	}

	public static void putResults(String martServiceLocation, String requestId,
			Query query, ResultReceiver resultReceiver) throws MartServiceException, ResultReceiverException {
		int attributeCount = getAttributeCount(query.getAttributes());
		boolean count = query.getCount() == 1;
		// if there are no attributes and we're not doing a count there's no
		// point in doing the query
		if (attributeCount > 0 || count) {
			String queryXml = queryToXML(query);
			logger.info(queryXml);
			NameValuePair[] data = { new NameValuePair(QUERY_ATTRIBUTE,
					queryXml) };
			PostMethod method = new PostMethod(martServiceLocation);
			method.setRequestBody(data);

			try {
				InputStream in = executeMethod(method, martServiceLocation);
				if (query.getFormatter() == null) {
					if (count) {
						resultReceiver.receiveResult(tabSeparatedReaderToResults(new InputStreamReader(in), 1), 0);
					} else {
						List<Attribute> attributes = query.getAttributes();
						Object[] result = new Object[attributes.size()];
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
						String line = bufferedReader.readLine();
						for (long i = 0; line != null; line = bufferedReader.readLine(), i++) {
							String[] tokens = line.split("\t", -1);
							if (attributeCount == tokens.length) {
								for (int ri = 0, ti = 0; ri < result.length && ti < tokens.length; ri++) {
									Attribute attribute = attributes.get(ri);
									if (attribute.getAttributes() == null) {
										result[ri] = tokens[ti];
										ti++;
									} else {
										int nestedAttributeCount = attribute.getAttributesCount();
										List<Object> list = new ArrayList<Object>();
										for (int j = 0; j < nestedAttributeCount; j++) {
											list.add(tokens[ti]);
											ti++;
										}
										result[ri] = list;
									}
								}
								resultReceiver.receiveResult(result, i);
							} else {
								resultReceiver.receiveError(line, i);
							}
						}
					}
				} else {
					resultReceiver.receiveResult(readResult(in, query.getFormatter()), 0);
				}
				in.close();
			} catch (IOException e) {
				String errorMessage = "Error reading data from "
						+ martServiceLocation;
				throw new MartServiceException(errorMessage, e);
			} finally {
				method.releaseConnection();
			}

		}
	}
	
//	private static String getLocation(MartURLLocation martUrlLocation) {
//		StringBuffer location = new StringBuffer("http://");
//		location.append(martUrlLocation.getHost());
//		location.append(":" + martUrlLocation.getPort());
//		location.append(martUrlLocation.getPath());
//		return location.toString();
//	}
	
	private static int getAttributeCount(List<Attribute> attributeList) {
		int result = 0;
		for (Attribute attribute : attributeList) {
			if (attribute.getAttributes() == null) {
				result++;
			} else {
				result += attribute.getAttributesCount();
			}
		}
		return result;
	}

	private static Object[] reassembleAttributeLists(Object[] lists, Query query) {
		int index = 0;
		List<Object> result = new ArrayList<Object>();
		for (Attribute attribute : query.getAttributes()) {
			if (attribute.getAttributes() == null) {
				result.add(lists[index]);
				index++;
			} else {
				int attributesCount = attribute.getAttributesCount();
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < attributesCount; i++) {
					list.add(lists[index]);
					index++;
				}
				result.add(list);
			}
		}
		return result.toArray();
	}

	public static Query splitAttributeLists(Query query) {
		Query result = new Query(query);
		for (Dataset dataset : result.getDatasets()) {
			List<Attribute> attributeList = dataset.getAttributes();
			dataset.removeAllAttributes();
			for (Attribute attribute : attributeList) {
				if (attribute.getAttributes() == null) {
					dataset.addAttribute(attribute);
				} else {
					String[] attributes = attribute.getAttributes().split(",");
					for (int i = 0; i < attributes.length; i++) {
						dataset.addAttribute(new Attribute(attributes[i]));
					}
				}
			}
		}
		return result;
	}

	public static String getMimeTypeForFormatter(String formatter) {
		String mimeType = "'text/plain'";
		if ("ADF".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("AXT".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("AXTPLUS".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("CSV".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("FASTA".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("FASTACDNA".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("GFF".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("HTML".equals(formatter)) {
			mimeType = "'text/html'";
		} else if ("MAF".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("MFA".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("MFASTA".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("TSV".equals(formatter)) {
			mimeType = "'text/tab-separeted-values'";
		} else if ("TXT".equals(formatter)) {
			mimeType = "'text/plain'";
		} else if ("XLS".equals(formatter)) {
			mimeType = "'application/excel'";
		}
		
		return mimeType;
	}
	
	private static List<String>[] tabSeparatedReaderToResults(Reader reader,
			int resultsCount) throws IOException {
		List<String>[] results = new List[resultsCount];
		for (int i = 0; i < results.length; i++) {
			results[i] = new ArrayList<String>();
		}
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = bufferedReader.readLine();
		while (line != null) {
			String[] tokens = line.split("\t", -1);
			for (int i = 0; i < results.length && i < tokens.length; i++) {
				results[i].add(tokens[i]);
			}
			line = bufferedReader.readLine();
		}
		return results;
	}

	private static Object[] readResult(InputStream inputStream, String formatter) throws IOException {
		Object[] result = new Object[1];
		
		if (getMimeTypeForFormatter(formatter).contains("application/")) {
			result[0] = IOUtils.toByteArray(inputStream);
		} else {
			result[0] = IOUtils.toString(inputStream);
		}
		return result;
	}
	
	private static MartDataset[] tabSeparatedReaderToDatasets(Reader reader,
			MartURLLocation martURLLocation) throws IOException {
		List<MartDataset> datasetList = new ArrayList<MartDataset>();
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = bufferedReader.readLine();
		while (line != null) {
			String[] tokens = line.split("\t");

			if (tokens.length >= 7) {
				MartDataset dataset = new MartDataset();

				dataset.setType(tokens[0]);
				dataset.setName(tokens[1]);
				dataset.setDisplayName(tokens[2]);
				if (tokens[3].equals("1")) {
					dataset.setVisible(true);
				} else {
					dataset.setVisible(false);
				}
				// value at position 4 is not documented so it's skipped
				try {
					dataset.setInitialBatchSize(Long.parseLong(tokens[5]));
				} catch (NumberFormatException e) {
					dataset.setInitialBatchSize(0);
				}
				try {
					dataset.setMaximumBatchSize(Long.parseLong(tokens[6]));
				} catch (NumberFormatException e) {
					dataset.setMaximumBatchSize(0);
				}

				if (tokens.length > 7) {
					dataset.setInterface(tokens[7]);
					dataset.setModified(tokens[8]);
				}

				dataset.setMartURLLocation(martURLLocation);

				datasetList.add(dataset);
			}
			line = bufferedReader.readLine();
		}
		return datasetList.toArray(new MartDataset[datasetList.size()]);
	}

	/**
	 * Creates an XML string from a query
	 * 
	 * @param query
	 * @return an XML string
	 */
	public static String queryToXML(Query query) {
		Document document = new Document(QueryXMLHandler.queryToElement(query,
				Namespace.NO_NAMESPACE), new DocType("Query"));
		return new XMLOutputter().outputString(document);
	}

	/**
	 * @param martServiceLocation
	 * @param data
	 * @return
	 * @throws MartServiceException
	 */
	private static InputStream executeMethod(HttpMethod method,
			String martServiceLocation) throws MartServiceException {
		HttpClient client = new HttpClient();
		if (isProxyHost(martServiceLocation)) {
			setProxy(client);
		}

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
//		method.getParams().setSoTimeout(60000);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				throw constructException(method, martServiceLocation, null);
			}
			return method.getResponseBodyAsStream();
		} catch (IOException e) {
			throw constructException(method, martServiceLocation, e);
		}
	}

	/**
	 * 
	 * @param client
	 */
	public static void setProxy(HttpClient client) {
		String host = System.getProperty("http.proxyHost");
		String port = System.getProperty("http.proxyPort");
		String user = System.getProperty("http.proxyUser");
		String password = System.getProperty("http.proxyPassword");

		if (host != null && port != null) {
			try {
				int portInteger = Integer.parseInt(port);
				client.getHostConfiguration().setProxy(host, portInteger);
				if (user != null && password != null) {
					client.getState().setProxyCredentials(
							new AuthScope(host, portInteger),
							new UsernamePasswordCredentials(user, password));
				}
			} catch (NumberFormatException e) {
				logger.error("Proxy port not an integer", e);
			}
		}
	}

	public static boolean isProxyHost(String location) {
		String httpNonProxyHosts = System.getProperty("http.nonProxyHosts");
		if (httpNonProxyHosts != null) {
			try {
				String host = new URL(location).getHost();
				String[] nonProxyHosts = httpNonProxyHosts.split("\\|");
				for (int i = 0; i < nonProxyHosts.length; i++) {
					if (nonProxyHosts[i].startsWith("*")) {
						if (host.endsWith(nonProxyHosts[i].substring(1))) {
							return false;
						}
					} else if (nonProxyHosts[i].endsWith("*")) {
						if (host.startsWith(nonProxyHosts[i].substring(0,
								nonProxyHosts[i].length() - 1))) {
							return false;
						}
					} else {
						if (host.equals(nonProxyHosts[i])) {
							return false;
						}
					}
				}
			} catch (MalformedURLException e) {
				logger.warn("'" + location + "' is not a valid URL. "
						+ "Cannot compare host with http.nonProxyHosts", e);
			}
		}
		return true;
	}

	private static MartServiceException constructException(HttpMethod method,
			String martServiceLocation, Exception cause) {
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("Error posting to " + martServiceLocation
				+ lineSeparator);
		if (cause == null) {
			errorMessage.append(" " + method.getStatusLine()
					+ lineSeparator);
		}
		if (method instanceof PostMethod) {
			PostMethod postMethod = (PostMethod) method;
			NameValuePair[] data = postMethod.getParameters();
			for (int i = 0; i < data.length; i++) {
				errorMessage.append(" " + data[i].getName() + " = "
						+ data[i].getValue()
						+ lineSeparator);
			}

		} else {
			errorMessage.append(method.getQueryString());
		}
		return new MartServiceException(errorMessage.toString(), cause);
	}

}
