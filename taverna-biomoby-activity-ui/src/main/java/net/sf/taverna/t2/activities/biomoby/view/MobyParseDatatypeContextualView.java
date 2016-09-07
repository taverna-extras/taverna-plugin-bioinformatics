/**
 *
 */
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

import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivityConfigurationBean;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class MobyParseDatatypeContextualView extends
		HTMLBasedActivityContextualView<MobyParseDatatypeActivityConfigurationBean> {

	public MobyParseDatatypeContextualView(Activity<?> activity, ColourManager colourManager) {
		super(activity, colourManager);
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "<tr><td>Article name used by service</td><td>"
				+ getConfigBean().getArticleNameUsedByService() + "</td></tr>";
		html += "<tr><td>Datatype</td><td>" + getConfigBean().getDatatypeName() + "</td></tr>";
		html += "<tr><td>Registry endpoint</td><td>" + getConfigBean().getRegistryEndpoint()
				+ "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Moby parse datatype service";
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

}
