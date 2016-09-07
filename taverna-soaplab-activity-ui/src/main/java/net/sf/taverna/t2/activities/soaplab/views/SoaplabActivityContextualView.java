package net.sf.taverna.t2.activities.soaplab.views;
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

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.swing.Action;
import javax.xml.namespace.QName;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.taverna.t2.activities.soaplab.actions.SoaplabActivityConfigurationAction;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import org.apache.taverna.scufl2.api.activity.Activity;
import org.apache.taverna.scufl2.api.configurations.Configuration;

import com.fasterxml.jackson.databind.JsonNode;

public class SoaplabActivityContextualView extends HTMLBasedActivityContextualView {

	private static Logger logger = Logger.getLogger(SoaplabActivityContextualView.class);

	private static final long serialVersionUID = -6470801873448104509L;

	private final EditManager editManager;

	private final FileManager fileManager;

	private final ActivityIconManager activityIconManager;

	private final ServiceDescriptionRegistry serviceDescriptionRegistry;

	public SoaplabActivityContextualView(Activity activity, EditManager editManager,
			FileManager fileManager, ActivityIconManager activityIconManager,
			ColourManager colourManager, ServiceDescriptionRegistry serviceDescriptionRegistry) {
		super(activity, colourManager);
		this.editManager = editManager;
		this.fileManager = fileManager;
		this.activityIconManager = activityIconManager;
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

	@Override
	public String getViewTitle() {
		return "Soaplab service";
	}

	@Override
	protected String getRawTableRowsHtml() {
		Configuration configuration = getConfigBean();
		JsonNode json = configuration.getJson();
		String html = "<tr><td>Endpoint</td><td>" + json.get("endpoint").textValue() + "</td></tr>";
		html += "<tr><td>Polling interval</td><td>" + json.get("pollingInterval").asText()
				+ "</td></tr>";
		html += "<tr><td>Polling backoff</td><td>" + json.get("pollingBackoff").asText()
				+ "</td></tr>";
		html += "<tr><td>Polling interval max</td><td>" + json.get("pollingIntervalMax").asText()
				+ "</td></tr>";
		// html += "<tr><td>SOAPLAB Metadata</td><td>" + getMetadata()
		// + "</td></tr>";
		return html;
	}

	@Override
	public Action getConfigureAction(Frame owner) {
		return new SoaplabActivityConfigurationAction(getActivity(), owner, editManager,
				fileManager, activityIconManager, serviceDescriptionRegistry);
	}

	private String getMetadata() {
		try {
			Configuration configuration = getConfigBean();
			JsonNode json = configuration.getJson();
			String endpoint = json.get("endpoint").textValue();
			Call call = (Call) new Service().createCall();
			call.setTimeout(new Integer(0));
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName("describe"));
			String metadata = (String) call.invoke(new Object[0]);
			logger.info(metadata);
			// Old impl, returns a tree of the XML
			// ColXMLTree tree = new ColXMLTree(metadata);
			URL sheetURL = SoaplabActivityContextualView.class
					.getResource("/analysis_metadata_2_html.xsl");
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			logger.info(sheetURL.toString());
			Templates stylesheet = transformerFactory.newTemplates(new StreamSource(sheetURL
					.openStream()));
			Transformer transformer = stylesheet.newTransformer();
			StreamSource inputStream = new StreamSource(new ByteArrayInputStream(
					metadata.getBytes()));
			ByteArrayOutputStream transformedStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(transformedStream);
			transformer.transform(inputStream, result);
			transformedStream.flush();
			transformedStream.close();
			// String summaryText = "<html><head>"
			// + WorkflowSummaryAsHTML.STYLE_NOBG + "</head>"
			// + transformedStream.toString() + "</html>";
			// JEditorPane metadataPane = new ColJEditorPane("text/html",
			// summaryText);
			// metadataPane.setText(transformedStream.toString());
			// // logger.info(transformedStream.toString());
			// JScrollPane jsp = new JScrollPane(metadataPane,
			// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			// JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			// jsp.setPreferredSize(new Dimension(0, 0));
			// jsp.getVerticalScrollBar().setValue(0);
			return transformedStream.toString();
		} catch (Exception ex) {
			return "<font color=\"red\">Error</font><p>An exception occured while trying to fetch Soaplab metadata from the server. The error was :<pre>"
					+ ex.getMessage() + "</pre>";

		}
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

}
