package net.sf.taverna.t2.activities.biomoby.view;
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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactoryRegistry;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.Before;
import org.junit.Test;

public class BiomobyContextualViewFactoryTest {
	BiomobyActivity activity;
	@Before
	public void setup() throws ActivityConfigurationException {
		activity=new BiomobyActivity() { //need to prevent the activity trying to configure itself, but store a copy of the config bean

			@Override
			public void configure(
					BiomobyActivityConfigurationBean configurationBean)
					throws ActivityConfigurationException {
				this.configurationBean=configurationBean;
			}

		};
		BiomobyActivityConfigurationBean b = new BiomobyActivityConfigurationBean();
		b.setAuthorityName("a");
		b.setMobyEndpoint("e");
		b.setServiceName("s");
		activity.configure(b);
	}

	@Test
	public void testGetConfigureAction() throws Exception {
		ContextualView view = new BiomobyActivityContextualView(activity, null, null, null, null);
		//will be null because its not a valid activity so therefore has no secondaries
		assertNull("The action should be null",view.getConfigureAction(null));
	}
}
