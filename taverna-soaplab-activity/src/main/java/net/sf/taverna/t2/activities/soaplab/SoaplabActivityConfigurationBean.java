/*******************************************************************************
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
@ConfigurationBean(uri = SoaplabActivity.URI + "#Config")
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
