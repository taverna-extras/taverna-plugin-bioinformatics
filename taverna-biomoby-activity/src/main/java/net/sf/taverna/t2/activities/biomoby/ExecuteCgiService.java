/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.io.BufferedReader;
import java.io.InputStreamReader;
/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.biomoby.shared.MobyException;

/**
 * This class contains one method that is used to execute synchronous HTTP POST
 * services
 * 
 * @author Edward Kawas
 * 
 */

public class ExecuteCgiService {

	/**
	 * 
	 * @param serviceEndpoint
	 *            the URL to the HTTP POST service
	 * @param xml
	 *            the XML to send the service
	 * @return a string representing the output from the service
	 * @throws MobyException
	 *             if anything goes wrong (problems reading/writing to the
	 *             service)
	 */
	public static String executeCgiService(String serviceEndpoint, String xml)
			throws MobyException {
		try {
			// Construct data
			String data = "data=" + URLEncoder.encode(xml, "UTF-8");

			// Send data
			URL url = new URL(serviceEndpoint);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			wr.close();
			rd.close();
			return sb.toString();
		} catch (Exception e) {
			throw new MobyException(e.getMessage());
		}
	}
}
