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
package net.sf.taverna.t2.activities.biomart.views;

import java.awt.Frame;
import java.net.URI;

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
import org.jdom.input.DOMBuilder;

import uk.org.taverna.configuration.app.ApplicationConfiguration;
import uk.org.taverna.scufl2.api.activity.Activity;
import uk.org.taverna.scufl2.api.property.MultiplePropertiesException;
import uk.org.taverna.scufl2.api.property.PropertyLiteral;
import uk.org.taverna.scufl2.api.property.PropertyNotFoundException;
import uk.org.taverna.scufl2.api.property.UnexpectedPropertyException;

public class BiomartActivityContextualView extends HTMLBasedActivityContextualView {

	private static final URI ACTIVITY_TYPE = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");
	private static final long serialVersionUID = -33919649695058443L;
	private final EditManager editManager;
	private final FileManager fileManager;
	private final ActivityIconManager activityIconManager;
	private final ApplicationConfiguration applicationConfiguration;
	private final ServiceDescriptionRegistry serviceDescriptionRegistry;

	public BiomartActivityContextualView(Activity activity, EditManager editManager,
			FileManager fileManager, ActivityIconManager activityIconManager,
			ColourManager colourManager, ApplicationConfiguration applicationConfiguration,
			ServiceDescriptionRegistry serviceDescriptionRegistry) {
		super(activity, colourManager);
		this.editManager = editManager;
		this.fileManager = fileManager;
		this.activityIconManager = activityIconManager;
		this.applicationConfiguration = applicationConfiguration;
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

	@Override
	protected String getRawTableRowsHtml() {
		PropertyLiteral propertyLiteral = null;
		try {
			propertyLiteral = getConfigBean().getPropertyResource().getPropertyAsLiteral(ACTIVITY_TYPE.resolve("#martQuery"));
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
		return new BiomartActivityConfigurationAction(getActivity(), owner,
				editManager, fileManager, activityIconManager, applicationConfiguration, serviceDescriptionRegistry);
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

}
