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
import java.util.List;

import net.sf.taverna.t2.activities.biomart.BiomartActivity;
import net.sf.taverna.t2.activities.biomart.BiomartActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomart.actions.BiomartActivityConfigurationAction;
import net.sf.taverna.t2.activities.biomart.views.BiomartActivityContextualView;
import net.sf.taverna.t2.activities.biomart.views.BiomartActivityViewFactory;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactoryRegistry;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.impl.ContextualViewFactoryRegistryImpl;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;

public class TestBiomartActivityContextualView {
	Activity<?> activity;
	@Before
	public void setup() throws Exception {
		activity = new BiomartActivity();

		Element el = getQueryElement("biomart-query.xml");

		BiomartActivityConfigurationBean configuration = new BiomartActivityConfigurationBean();
		configuration.setMartQuery(el);

		((BiomartActivity)activity).configure(configuration);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDisovery() throws Exception {


		List<ContextualViewFactory> viewFactoriesForBeanType = new ContextualViewFactoryRegistryImpl().getViewFactoriesForObject(activity);
		assertTrue("The biomart view factory should not be empty", !viewFactoriesForBeanType.isEmpty());
		BiomartActivityViewFactory factory = null;
		for (ContextualViewFactory cvf : viewFactoriesForBeanType) {
			if (cvf instanceof BiomartActivityViewFactory) {
				factory = (BiomartActivityViewFactory) cvf;
			}
		}
		assertTrue("No Biomart view factory", factory != null);
	}

	@Test
	public void testConfigurationAction() throws Exception {
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null);
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
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null);
		view.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		new TestBiomartActivityContextualView().run();
	}

}
