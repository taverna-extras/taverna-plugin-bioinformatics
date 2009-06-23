/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.soaplab;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.health.HealthReport;
import net.sf.taverna.t2.workflowmodel.health.HealthReport.Status;

public class SoaplabActivityHealthChecker implements HealthChecker<SoaplabActivity> {

	public boolean canHandle(Object subject) {
		return subject!=null && subject instanceof SoaplabActivity;
	}

	public HealthReport checkHealth(SoaplabActivity activity) {
		return testEndpoint(activity);
	}

	private int pingURL(HttpURLConnection httpConnection, int timeout)
			throws IOException {
		httpConnection.setRequestMethod("HEAD");
		httpConnection.connect();
		httpConnection.setReadTimeout(timeout);
		return httpConnection.getResponseCode();
	}

	private HealthReport testEndpoint(SoaplabActivity activity) {
		HealthReport report;
		String endpoint = activity.getConfiguration().getEndpoint();

		try {
			URL url = new URL(endpoint);
			URLConnection connection = url.openConnection();
			if (connection instanceof HttpURLConnection) {
				int code = pingURL((HttpURLConnection) connection, 15000);
				if (code == 200) {
					report = new HealthReport("SOAPLab Activity",
							"The endpoint [" + endpoint
									+ "] responded with a response code of "
									+ code, Status.OK);

				} else {
					report = new HealthReport("SOAPLab Activity",
							"The endpoint [" + endpoint
									+ "] responded, but a response code of "
									+ code, Status.WARNING);
				}
			}
			else {
				return new HealthReport("SOAPLab Activity","The endpoint["+endpoint+"] is not Http based and could not be tested for a http response",Status.OK);
			}
		} catch (MalformedURLException e) {
			report = new HealthReport("SOAPLab Activity",
					"There was a problem with the endpoint[" + endpoint
							+ "] URL:" + e.getMessage(), Status.SEVERE);
		} catch (SocketTimeoutException e) {
			report = new HealthReport("SOAPLab Activity", "The endpoint["
					+ endpoint + "] took more than 15 seconds to respond",
					Status.SEVERE);
		} catch (IOException e) {
			report = new HealthReport("SOAPLab Activity",
					"There was an error contacting the endpoint[" + endpoint
							+ "]:" + e.getMessage(), Status.SEVERE);
		}

		return report;
	}
}
