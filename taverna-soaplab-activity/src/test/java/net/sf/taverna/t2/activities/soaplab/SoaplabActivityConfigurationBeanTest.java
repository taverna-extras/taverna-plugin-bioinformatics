package net.sf.taverna.t2.activities.soaplab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for SoaplabActivityConfigurationBean.
 * 
 * @author David Withers
 */
public class SoaplabActivityConfigurationBeanTest {

	private SoaplabActivityConfigurationBean bean;
	
	private String endpoint = "http://www.ebi.ac.uk/soaplab/emboss4/services/utils_misc.embossversion";

	@Before
	public void setUp() throws Exception {
		bean  = new SoaplabActivityConfigurationBean();
	}

	@Test
	public void testGetEndpoint() {
		assertNull(bean.getEndpoint());
	}

	@Test
	public void testSetEndpoint() {
		bean.setEndpoint(endpoint);
		assertEquals(endpoint, bean.getEndpoint());
		bean.setEndpoint(null);
		assertNull(bean.getEndpoint());

	}

	@Test
	public void testGetPollingInterval() {
		assertEquals(0, bean.getPollingInterval());
	}

	@Test
	public void testSetPollingInterval() {
		bean.setPollingInterval(2000);
		assertEquals(2000, bean.getPollingInterval());
	}

	@Test
	public void testGetPollingBackoff() {
		assertEquals(1.0, bean.getPollingBackoff(), 0);
	}

	@Test
	public void testSetPollingBackoff() {
		bean.setPollingBackoff(1.4);
		assertEquals(1.4, bean.getPollingBackoff(), 0);
	}

	@Test
	public void testGetPollingIntervalMax() {
		assertEquals(0, bean.getPollingIntervalMax());
	}

	@Test
	public void testSetPollingIntervalMax() {
		bean.setPollingInterval(5000);
		assertEquals(5000, bean.getPollingInterval());
	}

}
