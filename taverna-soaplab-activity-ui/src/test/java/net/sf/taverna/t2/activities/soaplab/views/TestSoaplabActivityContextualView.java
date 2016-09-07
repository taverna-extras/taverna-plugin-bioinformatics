package net.sf.taverna.t2.activities.soaplab.views;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.taverna.t2.activities.soaplab.actions.SoaplabActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.taverna.scufl2.api.activity.Activity;

public class TestSoaplabActivityContextualView {

	Activity a;

	@Before
	public void setup() throws Exception {
		a=new Activity();
	}

	@Test
	@Ignore("Integration test")
	public void testConfigureAction() throws Exception {
		ContextualView view = new SoaplabActivityContextualView(a, null, null, null, null, null);
		assertNotNull("the action should not be null",view.getConfigureAction(null));
		assertTrue("The action should be a SoaplabAcitivyConfigurationAction",view.getConfigureAction(null) instanceof SoaplabActivityConfigurationAction);
	}

	private void run() throws Exception
	{
		setup();
		ContextualView view = new SoaplabActivityContextualView(a, null, null, null, null, null);
		view.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new TestSoaplabActivityContextualView().run();
	}
}
