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

import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationBean;
import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationProperty;

import org.jdom.Element;

/**
 * Biomart configuration.
 *
 * @author David Withers
 */
@ConfigurationBean(uri = BiomartActivity.URI + "#Config")
public class BiomartActivityConfigurationBean {

	private Element martQuery;

	public Element getMartQuery() {
		return martQuery;
	}

	@ConfigurationProperty(name = "martQuery", label = "Mart Query", description = "Biomart query in XML")
	public void setMartQuery(Element martQuery) {
		this.martQuery = martQuery;
	}

}
