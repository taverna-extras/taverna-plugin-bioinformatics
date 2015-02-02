/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

/**
 * This class contains one method that is used to execute synchronous moby
 * services
 * 
 * @author Edward Kawas
 * 
 */

public class ExecuteMobyService {

	/**
	 * 
	 * @param endpoint
	 *            the SOAP endpoint of the service to call
	 * @param service
	 *            the name of the service
	 * @param xml
	 *            the XML to send the service
	 * @return a string of XML representing the output from the service given
	 *         our input
	 * @throws MobyException
	 *             if anything goes wrong (SOAP error)
	 */
	public static String executeMobyService(String endpoint, String service,
			String xml) throws MobyException {
		return new CentralImpl(endpoint, "http://biomoby.org/").call(service,
				xml);
	}
}
