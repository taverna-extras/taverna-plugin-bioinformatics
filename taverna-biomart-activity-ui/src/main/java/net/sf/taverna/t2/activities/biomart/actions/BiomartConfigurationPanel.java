/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart.actions;

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
