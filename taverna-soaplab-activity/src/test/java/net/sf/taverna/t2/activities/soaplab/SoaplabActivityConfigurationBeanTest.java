package net.sf.taverna.t2.activities.soaplab;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
