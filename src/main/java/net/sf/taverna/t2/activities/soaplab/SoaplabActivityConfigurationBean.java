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

import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationBean;
import net.sf.taverna.t2.workflowmodel.processor.config.ConfigurationProperty;

/**
 * A configuration bean specific to a Soaplab activity. In particular it provides details
 * about the Soaplab service endpoint and the polling settings.
 * 
 * @author David Withers
 */
@ConfigurationBean(uri = SoaplabActivity.URI + "/configuration")
public class SoaplabActivityConfigurationBean {

	private String endpoint = null;

	private int pollingInterval = 0;

	private double pollingBackoff = 1.0;

	private int pollingIntervalMax = 0;

	/**
	 * Returns the endpoint.
	 *
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Sets the endpoint.
	 *
	 * @param endpoint the new endpoint
	 */
	@ConfigurationProperty(name = "endpoint", label = "Soaplab Service Endpoint", description = "The endpoint of the Soaplab service")
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Returns the pollingInterval.
	 *
	 * @return the pollingInterval
	 */
	public int getPollingInterval() {
		return pollingInterval;
	}

	/**
	 * Sets the pollingInterval.
	 *
	 * @param pollingInterval the new pollingInterval
	 */
	@ConfigurationProperty(name = "pollingInterval", label = "Polling Interval", description = "The polling time interval (in milliseconds)", required = false)
	public void setPollingInterval(int pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	/**
	 * Returns the pollingBackoff.
	 *
	 * @return the pollingBackoff
	 */
	public double getPollingBackoff() {
		return pollingBackoff;
	}

	/**
	 * Sets the pollingBackoff.
	 *
	 * @param pollingBackoff the new pollingBackoff
	 */
	@ConfigurationProperty(name = "pollingBackoff", label = "Polling Backoff", description = "The polling backoff factor", required = false)
	public void setPollingBackoff(double pollingBackoff) {
		this.pollingBackoff = pollingBackoff;
	}

	/**
	 * Returns the pollingIntervalMax.
	 *
	 * @return the pollingIntervalMax
	 */
	public int getPollingIntervalMax() {
		return pollingIntervalMax;
	}

	/**
	 * Sets the pollingIntervalMax.
	 *
	 * @param pollingIntervalMax the new pollingIntervalMax
	 */
	@ConfigurationProperty(name = "pollingIntervalMax", label = "Max Polling Interval", description = "The maximum polling time interval (in milliseconds)", required = false)
	public void setPollingIntervalMax(int pollingIntervalMax) {
		this.pollingIntervalMax = pollingIntervalMax;
	}

}
