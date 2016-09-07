package net.sf.taverna.t2.activities.biomart.actions;
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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import org.apache.log4j.Logger;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.config.QueryConfigController;
import org.biomart.martservice.config.ui.MartServiceQueryConfigUIFactory;
import org.biomart.martservice.config.ui.QueryConfigUIFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import uk.org.taverna.commons.services.ServiceRegistry;
import uk.org.taverna.configuration.app.ApplicationConfiguration;
import org.apache.taverna.scufl2.api.activity.Activity;

public class BiomartConfigurationPanel extends ActivityConfigurationPanel {
	private static Logger logger = Logger.getLogger(BiomartConfigurationPanel.class);

	private static final long serialVersionUID = 1884045346293327621L;

	public static final URI ACTIVITY_TYPE = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");

	private String configurationString;

	private MartQuery biomartQuery;

	private static XMLOutputter outputter = new XMLOutputter();

	private final ApplicationConfiguration applicationConfiguration;

	private final ServiceRegistry serviceRegistry;

	public BiomartConfigurationPanel(Activity activity, ApplicationConfiguration applicationConfiguration,
			ServiceRegistry serviceRegistry) {
		super(activity);
		this.applicationConfiguration = applicationConfiguration;
		this.serviceRegistry = serviceRegistry;
		initialise();
	}

	protected void initialise() {
		super.initialise();
		removeAll();

		configurationString = getProperty("martQuery");
		Element martQuery = null;
		try {
			martQuery = new SAXBuilder().build(new StringReader(configurationString)).getRootElement();
		} catch (JDOMException | IOException e) {
			logger.warn(e);
		}
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

		biomartQuery = MartServiceXMLHandler.elementToMartQuery(martQuery, null);
		MartService service = biomartQuery.getMartService();

		File homeRoot=applicationConfiguration.getApplicationHomeDir();
		if (homeRoot==null) {
			logger.error("unable to determine application home for biomart cache");
			homeRoot=new File(System.getProperty("java.io.tmpdir"));
		}
		File cache=new File(homeRoot,"t2-biomart-activity");
		service.setCacheDirectory(new File(cache,"cache"));
		logger.info("Biomart is using cache directory:"+cache.getAbsolutePath());

		QueryConfigController controller = new QueryConfigController(biomartQuery);
		try {
			QueryConfigUIFactory queryConfigUIFactory = new MartServiceQueryConfigUIFactory(
					service, controller, biomartQuery.getMartDataset());
			add(queryConfigUIFactory.getDatasetConfigUI());
			add(Box.createGlue());
		} catch (MartServiceException e) {
			add(new JLabel("Error reading configuration properties"));
			add(new JLabel(e.getMessage()));
			add(Box.createGlue());
		}
		this.setPreferredSize(new Dimension(900,500));
		this.validate();
	}

	public Element getQuery() {
		return MartServiceXMLHandler.martQueryToElement(biomartQuery, null);
	}

	@Override
	public void noteConfiguration() {
		setProperty("martQuery", outputter.outputString(new Document(getQuery())));
		configureInputPorts(serviceRegistry);
		configureOutputPorts(serviceRegistry);
	}

	@Override
	public boolean checkValues() {
		// TODO Not yet done
		return true;
	}
}
