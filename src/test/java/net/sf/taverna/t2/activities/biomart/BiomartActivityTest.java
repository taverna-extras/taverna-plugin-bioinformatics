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
package net.sf.taverna.t2.activities.biomart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;

import org.biomart.martservice.MartServiceXMLHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Biomart Activity Tests
 *
 * @author David Withers
 *
 */
public class BiomartActivityTest {

	@Ignore("Integration test that requires network access")
	@Test
	public void simpleQuery() throws Exception {
		BiomartActivity activity = new BiomartActivity();

		BiomartActivityConfigurationBean configurationBean = new BiomartActivityConfigurationBean();
		configurationBean.setMartQuery(parseQuery("biomart-query.xml"));
		activity.configure(configurationBean);

		assertEquals(3, activity.getInputPorts().size());
		assertEquals(2, activity.getOutputPorts().size());

		Map<String,Object> inputs = new HashMap<String, Object>();
		Map<String, Class<?>> expectedOutputs = new HashMap<String, Class<?>>();
		expectedOutputs.put("hsapiens_gene_ensembl.chromosome_name", String.class);
		expectedOutputs.put("hsapiens_gene_ensembl.go_description", String.class);

		Map<String,Object> outputs = ActivityInvoker.invokeAsyncActivity(activity, inputs, expectedOutputs);
		assertTrue(outputs.containsKey("hsapiens_gene_ensembl.chromosome_name"));
		assertTrue(outputs.get("hsapiens_gene_ensembl.chromosome_name") instanceof List);
		assertTrue(((List<?>) outputs.get("hsapiens_gene_ensembl.chromosome_name")).size() > 0);
		assertTrue(outputs.containsKey("hsapiens_gene_ensembl.go_description"));
		assertTrue(outputs.get("hsapiens_gene_ensembl.go_description") instanceof List);
		assertTrue(((List<?>) outputs.get("hsapiens_gene_ensembl.go_description")).size() > 0);
	}

	private Element parseQuery(String resourceName) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		InputStream inStream = MartServiceXMLHandler.class.getResourceAsStream("/" + resourceName);
		Document document = builder.build(inStream);
		return document.getRootElement();
	}

}
