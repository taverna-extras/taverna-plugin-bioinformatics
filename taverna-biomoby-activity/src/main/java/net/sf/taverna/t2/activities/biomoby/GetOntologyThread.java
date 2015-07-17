/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.sf.taverna.t2.activities.biomoby;


import org.biomoby.client.CentralImpl;

/**
 * This class is used to speed up the running of workflows. Basically, whenever
 * a new Biomoby activity is added to taverna, a call out to RESOURCES/Objects
 * is made to download the datatype ontology.
 *
 * Uses BiomobyCache to process the registry
 *
 * This should speed up the execution of workflows, since the ontologies will
 * have already been downloaded.
 *
 * @author Eddie Kawas
 * @author Stuart Owen
 *
 * @see BiomobyCache
 *
 */
public class GetOntologyThread extends Thread {


	// the registry endpoint
	String worker = null;

	/**
	 *
	 * @param url
	 *            the registry endpoint URL
	 */
	public GetOntologyThread(String url) {
		super("BioMOBY GetOntologyThread");
		if (url == null || url.trim().equals(""))
			url = CentralImpl.getDefaultURL();
		this.worker = url;
		setDaemon(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		BiomobyCache.cacheForRegistryEndpoint(worker);
	}
}
