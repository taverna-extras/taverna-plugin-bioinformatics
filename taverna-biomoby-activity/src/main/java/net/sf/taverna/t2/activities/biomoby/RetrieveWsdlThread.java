/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
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
