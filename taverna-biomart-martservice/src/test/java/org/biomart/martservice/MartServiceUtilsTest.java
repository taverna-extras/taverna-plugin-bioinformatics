/*
package org.biomart.martservice;
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author David Withers
 */
public class MartServiceUtilsTest {

	/**
	 * Test method for {@link org.biomart.martservice.MartServiceUtils#isProxyHost(java.lang.String)}.
	 */
	@Test
	public void testIsProxyHost() {
		System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|*.mydomain.com|192.168.1.*");
		assertFalse(MartServiceUtils.isProxyHost("http://localhost/"));
		assertFalse(MartServiceUtils.isProxyHost("http://localhost:8080/"));
		assertFalse(MartServiceUtils.isProxyHost("http://127.0.0.1/"));
		assertFalse(MartServiceUtils.isProxyHost("http://www.mydomain.com/"));
		assertFalse(MartServiceUtils.isProxyHost("http://www.sub.mydomain.com/"));
		assertFalse(MartServiceUtils.isProxyHost("http://192.168.1.1/"));
		assertFalse(MartServiceUtils.isProxyHost("http://192.168.1.2/"));
		assertTrue(MartServiceUtils.isProxyHost("http://www.mydomain.co.uk/"));
		assertTrue(MartServiceUtils.isProxyHost("http://192.168.2.1/"));
		assertTrue(MartServiceUtils.isProxyHost("http://127.0.0.2/"));
		assertTrue(MartServiceUtils.isProxyHost("http://nonlocalhost/"));
	}

}
