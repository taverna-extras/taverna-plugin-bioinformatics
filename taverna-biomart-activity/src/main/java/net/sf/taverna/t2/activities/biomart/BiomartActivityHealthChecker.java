package net.sf.taverna.t2.activities.biomart;
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

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;

import net.sf.taverna.t2.workflowmodel.health.RemoteHealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.processor.activity.DisabledActivity;


import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceXMLHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class BiomartActivityHealthChecker extends RemoteHealthChecker {

	public boolean canVisit(Object subject) {
		if (subject == null) {
			return false;
		}
		if (subject instanceof BiomartActivity) {
			return true;
		}
		if (subject instanceof DisabledActivity) {
			return (((DisabledActivity) subject).getActivity() instanceof BiomartActivity);
		}
		return false;
	}

	public VisitReport visit(Object o, List<Object> ancestors) {
		Element biomartQueryElement = null;
		Activity activity = (Activity) o;
		if (activity instanceof BiomartActivity) {
			String martQueryText = ((BiomartActivity)activity).getConfiguration().get("martQuery").asText();
			SAXBuilder builder = new SAXBuilder();
			try {
				Document document = builder.build(new StringReader(martQueryText));
				biomartQueryElement = document.getRootElement();
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		} else if (activity instanceof DisabledActivity) {
			biomartQueryElement = (Element) ((DisabledActivity) activity).getActivityConfiguration();
		}
		MartQuery biomartQuery = MartServiceXMLHandler.elementToMartQuery(biomartQueryElement, null);
		return contactEndpoint(activity, biomartQuery.getMartService().getLocation());
	}


}
