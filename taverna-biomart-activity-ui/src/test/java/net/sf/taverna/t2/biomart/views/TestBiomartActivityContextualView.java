/*******************************************************************************
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
import org.jdom.output.XMLOutputter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.taverna.scufl2.api.activity.Activity;
import org.apache.taverna.scufl2.api.configurations.Configuration;
import org.apache.taverna.scufl2.api.profiles.Profile;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestBiomartActivityContextualView {
	Activity activity;

	@Before
	public void setup() throws Exception {
		activity = new Activity();
		activity.setParent(new Profile());

		Element el = getQueryElement("biomart-query.xml");

		Configuration configuration = new Configuration();
		configuration.setType(URI.create("http://ns.taverna.org.uk/2010/activity/biomart").resolve("#Config"));
		String queryText = new XMLOutputter().outputString(new Document(el));
		((ObjectNode) configuration.getJson()).put("martQuery", queryText);

		configuration.setConfigures(activity);
	}

	@Test @Ignore
	public void testConfigurationAction() throws Exception {
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null, null, null);
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
		BiomartActivityContextualView view = new BiomartActivityContextualView(activity, null, null, null, null, null, null, null);
		view.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new TestBiomartActivityContextualView().run();
	}

}
