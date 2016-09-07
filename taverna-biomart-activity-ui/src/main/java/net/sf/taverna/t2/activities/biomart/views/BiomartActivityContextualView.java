package net.sf.taverna.t2.activities.biomart.views;
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
import java.io.IOException;
import java.io.StringReader;

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconManager;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;

import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Filter;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import uk.org.taverna.commons.services.ServiceRegistry;
import uk.org.taverna.configuration.app.ApplicationConfiguration;
import org.apache.taverna.scufl2.api.activity.Activity;

@SuppressWarnings("serial")
public class BiomartActivityContextualView extends HTMLBasedActivityContextualView {

	private final EditManager editManager;
	private final FileManager fileManager;
	private final ActivityIconManager activityIconManager;
	private final ApplicationConfiguration applicationConfiguration;
	private final ServiceDescriptionRegistry serviceDescriptionRegistry;
	private final ServiceRegistry serviceRegistry;

	public BiomartActivityContextualView(Activity activity, EditManager editManager,
			FileManager fileManager, ActivityIconManager activityIconManager,
			ColourManager colourManager, ApplicationConfiguration applicationConfiguration,
			ServiceDescriptionRegistry serviceDescriptionRegistry, ServiceRegistry serviceRegistry) {
		super(activity, colourManager);
		this.editManager = editManager;
		this.fileManager = fileManager;
		this.activityIconManager = activityIconManager;
		this.applicationConfiguration = applicationConfiguration;
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	protected String getRawTableRowsHtml() {
		String queryText = getConfigBean().getJson().get("martQuery").textValue();
		Element martQuery = null;
		try {
			martQuery = new SAXBuilder().build(new StringReader(queryText)).getRootElement();
		} catch (JDOMException | IOException e) {
		}
		MartQuery q = MartServiceXMLHandler.elementToMartQuery(martQuery, null);
		String html = "<tr><td>URL</td><td>" + q.getMartService().getLocation() + "</td></tr>";
		html += "<tr><td>Location</td><td>"
				+ q.getMartDataset().getMartURLLocation().getDisplayName() + "</td></tr>";
		boolean firstFilter = true;
		for (Filter filter : q.getQuery().getFilters()) {
			html += firstFilter ? "<tr><td>Filter</td><td>" : "<tr><td></td></td>";
			firstFilter = false;
			html += filter.getName() + "</td></tr>";
		}
		boolean firstAttribute = true;
		for (Attribute attribute : q.getQuery().getAttributes()) {
			html += firstAttribute ? "<tr><td>Attribute</td><td>" : "<tr><td></td><td>";
			firstAttribute = false;
			html += attribute.getName() + "</td></tr>";
		}
		html += "<tr><td>Dataset</td><td>" + q.getMartDataset().getDisplayName() + "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Biomart service";
	}

	@Override
	public Action getConfigureAction(Frame owner) {
		return new BiomartActivityConfigurationAction(getActivity(), owner, editManager,
				fileManager, activityIconManager, applicationConfiguration,
				serviceDescriptionRegistry, serviceRegistry);
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

}
