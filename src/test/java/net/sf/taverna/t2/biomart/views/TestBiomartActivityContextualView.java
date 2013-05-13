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
package net.sf.taverna.t2.biomart.views;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.activities.biomart.views.BiomartActivityContextualView;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.org.taverna.scufl2.api.activity.Activity;
import uk.org.taverna.scufl2.api.configurations.Configuration;
import uk.org.taverna.scufl2.api.profiles.Profile;
import uk.org.taverna.scufl2.api.property.PropertyLiteral;

public class TestBiomartActivityContextualView {
	Activity activity;

	@Before
	public void setup() throws Exception {
		activity = new Activity();
		activity.setParent(new Profile());

		Element el = getQueryElement("biomart-query.xml");

		Configuration configuration = new Configuration();
		configuration.setType(URI.create("http://ns.taverna.org.uk/2010/activity/biomart").resolve("#Config"));
		org.w3c.dom.Element element = new DOMOutputter().output(new Document(el)).getDocumentElement();
		configuration.getPropertyResource().addProperty(URI.create("http://ns.taverna.org.uk/2010/activity/biomart").resolve("#martQuery"), new PropertyLiteral(element));

		configuration.setConfigures(activity);
	}

	@Test @Ignore
	public void testConfigurationAction() throws Exception {
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null, null);
		assertNotNull("The view should provide a configuration action",view.getConfigureAction(null));
		assertTrue("The configuration action should be an instance of BiomartActivityConfigurationAction",view.getConfigureAction(null) instanceof BiomartActivityConfigurationAction);
	}

	private Element getQueryElement(String resourceName) throws Exception {
		InputStream inStream = TestBiomartActivityContextualView.class
				.getResourceAsStream("/"+resourceName);

		if (inStream == null)
			throw new IOException(
					"Unable to find resource for:"
							+ resourceName);
		SAXBuilder builder = new SAXBuilder();
		return builder.build(inStream).detachRootElement();
	}

	private void run() throws Exception {
		setup();
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null, null);
		view.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new TestBiomartActivityContextualView().run();
	}

}
