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
package net.sf.taverna.t2.activities.biomart;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.health.HealthReport;
import net.sf.taverna.t2.workflowmodel.health.HealthReport.Status;

import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceXMLHandler;
import org.jdom.Element;

public class BiomartActivityHealthChecker implements HealthChecker<BiomartActivity> {

	public boolean canHandle(Object subject) {
		return subject!=null && subject instanceof BiomartActivity;
	}

	public HealthReport checkHealth(BiomartActivity activity) {
		Element biomartQueryElement = activity.getConfiguration();
		MartQuery biomartQuery = MartServiceXMLHandler.elementToMartQuery(biomartQueryElement, null);
		String location = biomartQuery.getMartService().getLocation();
		Status status = Status.OK;
		String message = "Responded OK";
		try {
			URL url = new URL(location);
			URLConnection connection = url.openConnection();
			if (connection instanceof HttpURLConnection) {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("HEAD");
				httpConnection.setReadTimeout(10000);
				httpConnection.connect();
				if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					if (httpConnection.getResponseCode() >= 400) {
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
			message = "Location is not a valid URL";
		} catch (SocketTimeoutException e) {
			status = Status.SEVERE;
			message = "Failed to respond within 10s";
		} catch (IOException e) {
			status = Status.SEVERE;
			message = "Error connecting : " + e.getMessage();
		}
		return new HealthReport("Biomart Activity [" + location + "]",
				message, status);
	}

}
