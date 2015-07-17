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

import org.biomoby.shared.MobyService;
import org.biomoby.shared.Central;

/**
 * This class is used to create a threaded call to the Biomoby registry that
 * retrieves WSDL for services. This is done, because the Biomoby API assumes
 * that before calling a service the WSDL is retrieved.
 *
 * @author Eddie Kawas
 *
 */
public class RetrieveWsdlThread extends Thread {
    // the service to get wsdl document
    MobyService service = null;

    // the registry that we will get the document from
    Central worker = null;

    /**
     * Constructor
     *
     * @param worker
     *                the Central registry that we query for wsdl
     * @param service
     *                the service that we want wsdl for
     */
    RetrieveWsdlThread(Central worker, MobyService service) {
	super("BioMOBY RetrieveWsdlThread");
	this.service = service;
	this.worker = worker;
	setDaemon(true);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
	try {
	    worker.getServiceWSDL(service.getName(), service.getAuthority());
	} catch (Exception e) {

	}
    }
}
