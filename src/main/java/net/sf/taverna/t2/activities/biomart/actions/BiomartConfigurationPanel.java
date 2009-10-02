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

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.raven.appconfig.ApplicationRuntime;

import org.apache.log4j.Logger;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.config.QueryConfigController;
import org.biomart.martservice.config.ui.MartServiceQueryConfigUIFactory;
import org.biomart.martservice.config.ui.QueryConfigUIFactory;
import org.jdom.Element;

public class BiomartConfigurationPanel extends JPanel {
	private static Logger logger = Logger
			.getLogger(BiomartConfigurationPanel.class);
	
	private static final long serialVersionUID = 1884045346293327621L;
	
	private Element bean;
	private JButton applyButton;
	private JButton closeButton;

	private MartQuery biomartQuery;

	public BiomartConfigurationPanel(Element bean) {
		this.bean = bean;
		initialise();
	}

	private void initialise() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		applyButton=new JButton("Apply");
		closeButton=new JButton("Close");
		
		biomartQuery = MartServiceXMLHandler.elementToMartQuery(bean, null);
		MartService service = biomartQuery.getMartService();
		
		File homeRoot=ApplicationRuntime.getInstance().getApplicationHomeDir();
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
			add(buttonBar());
		} catch (MartServiceException e) {
			add(new JLabel("Error reading configuration properties"));
			add(new JLabel(e.getMessage()));
			add(Box.createGlue());
		}
		
	}
	
	private JPanel buttonBar() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(applyButton);
		panel.add(closeButton);
		return panel;
	}

	public Element getQuery() {
		return MartServiceXMLHandler.martQueryToElement(biomartQuery, null);
	}

	public void setOkAction(Action okAction) {
		applyButton.setAction(okAction);
	}

	public void setCancelAction(Action cancelAction) {
		closeButton.setAction(cancelAction);
	}
}
