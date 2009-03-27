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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.biomoby.client.CentralImpl;
import org.biomoby.registry.meta.Registry;
import org.biomoby.shared.MobyDataType;

/**
 * This class is used to speed up the running of workflows. Basically, whenever
 * a new Biomoby activity is added to taverna, a call out to RESOURCES/Objects
 * is made to download the datatype ontology.
 * 
 * This should speed up the execution of workflows, since the ontologies will
 * have already been downloaded.
 * 
 * @author Eddie Kawas
 * 
 */
public class GetOntologyThread extends Thread {

	// a map of registries that we have already processed
	static Map<String, String> REGISTRIES_HANDLED = Collections
			.synchronizedMap(new HashMap<String, String>());

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
		if (REGISTRIES_HANDLED.containsKey(worker))
			return;
		try {
			Registry mRegistry = new Registry(worker, worker,
					"http://domain.com/MOBY/Central");
			// attempt to get a primitive datatype from the registry
			MobyDataType.getDataType("Float", mRegistry);
			// add the registry to the map
			REGISTRIES_HANDLED.put(worker, "");
		} catch (Exception e) {

		}
	}
}
