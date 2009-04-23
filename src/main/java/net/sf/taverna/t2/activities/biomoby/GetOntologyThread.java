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


import org.biomoby.client.CentralImpl;
import org.biomoby.registry.meta.Registry;

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
		
		try {
			
			BiomobyCache.cacheForRegistryEndpoint(worker);
		} catch (Exception e) {

		}
	}
}
