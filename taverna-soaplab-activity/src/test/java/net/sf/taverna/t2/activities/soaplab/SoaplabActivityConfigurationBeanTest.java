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
