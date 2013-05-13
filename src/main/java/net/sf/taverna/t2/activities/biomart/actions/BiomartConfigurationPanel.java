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
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
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
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;

import uk.org.taverna.configuration.app.ApplicationConfiguration;
import uk.org.taverna.scufl2.api.activity.Activity;
import uk.org.taverna.scufl2.api.common.Scufl2Tools;
import uk.org.taverna.scufl2.api.configurations.Configuration;
import uk.org.taverna.scufl2.api.property.MultiplePropertiesException;
import uk.org.taverna.scufl2.api.property.PropertyLiteral;
import uk.org.taverna.scufl2.api.property.PropertyNotFoundException;
import uk.org.taverna.scufl2.api.property.UnexpectedPropertyException;

public class BiomartConfigurationPanel extends ActivityConfigurationPanel {
	private static Logger logger = Logger.getLogger(BiomartConfigurationPanel.class);

	private static final long serialVersionUID = 1884045346293327621L;

	public static final URI ACTIVITY_TYPE = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");

	private Configuration configuration;
	private String configurationString;

	private MartQuery biomartQuery;

	private Activity activity;

	private static XMLOutputter outputter = new XMLOutputter();

	private final ApplicationConfiguration applicationConfiguration;

	private final ServiceDescription serviceDescription;

	private Scufl2Tools scufl2Tools = new Scufl2Tools();

	public BiomartConfigurationPanel(Activity activity, ApplicationConfiguration applicationConfiguration,
			ServiceDescription serviceDescription) {
		this.activity = activity;
		this.applicationConfiguration = applicationConfiguration;
		this.serviceDescription = serviceDescription;
		initialise();
	}

	private void initialise() {
		this.configuration = scufl2Tools.configurationFor(activity, activity.getParent());
		PropertyLiteral propertyLiteral = null;
		try {
			propertyLiteral = configuration.getPropertyResource().getPropertyAsLiteral(ACTIVITY_TYPE.resolve("#martQuery"));
		} catch (UnexpectedPropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PropertyNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MultiplePropertiesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Element martQuery = new DOMBuilder().build(propertyLiteral.getLiteralValueAsElement());
		this.configurationString = outputter.outputString(martQuery);
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
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public boolean isConfigurationChanged() {
		String queryString = outputter.outputString(getQuery());
		return !queryString.equals(configurationString);
	}

	@Override
	public void noteConfiguration() {
		Element martQuery = (Element) getQuery().clone();
		Configuration newConfiguration = serviceDescription.getActivityConfiguration();
		configuration.getPropertyResource().clearProperties(ACTIVITY_TYPE.resolve("#martQuery"));
		try {
			org.w3c.dom.Element element = new DOMOutputter().output(new Document(martQuery)).getDocumentElement();
			configuration.getPropertyResource().addProperty(ACTIVITY_TYPE.resolve("#martQuery"), new PropertyLiteral(element));
		} catch (JDOMException e) {
			logger.warn("Can't convert MartQuery to org.w3c.dom.Element", e);
		}
		configuration = newConfiguration;
		configurationString = outputter.outputString(martQuery);
	}

	@Override
	public void refreshConfiguration() {
		removeAll();
		initialise();
	}

	@Override
	public boolean checkValues() {
		// TODO Not yet done
		return true;
	}
}
