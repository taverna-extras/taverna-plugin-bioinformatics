/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.health.HealthReport;
import net.sf.taverna.t2.workflowmodel.health.HealthReport.Status;

/**
 * A health checker for the Moby Parse Datatype activity.
 * 
 * @author David Withers
 */
public class MobyParseDatatypeActivityHealthChecker implements HealthChecker<MobyParseDatatypeActivity> {

	private int timeoutInSeconds = 10;
	
	public boolean canHandle(Object activity) {
		return activity!=null && activity instanceof MobyParseDatatypeActivity;
	}

	public HealthReport checkHealth(MobyParseDatatypeActivity activity) {
		String endpoint = activity.getConfiguration().getRegistryEndpoint();
		Status status = Status.OK;
		String message = "Responded OK";
		try {
			URL url = new URL(endpoint);
			URLConnection connection = url.openConnection();
			if (connection instanceof HttpURLConnection) {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("HEAD");
				httpConnection.setReadTimeout(timeoutInSeconds * 1000);
				httpConnection.connect();
				int responseCode = httpConnection.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK &&
						responseCode != HttpURLConnection.HTTP_LENGTH_REQUIRED) {
					if (responseCode >= 400) {
						status = Status.SEVERE;
					} else {
						status = Status.WARNING;
					}
					message = "Responded with : "
							+ httpConnection.getResponseMessage();
				}
				httpConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			status = Status.SEVERE;
			message = "Endpoint is not a valid URL";
		} catch (SocketTimeoutException e) {
			status = Status.SEVERE;
			message = "Failed to respond within " + timeoutInSeconds + "s";
		} catch (IOException e) {
			status = Status.SEVERE;
			message = "Error connecting : " + e.getMessage();
		}
		return new HealthReport("Moby Parse Datatype Activity [" + endpoint + "]",
				message, status);
	}

}
