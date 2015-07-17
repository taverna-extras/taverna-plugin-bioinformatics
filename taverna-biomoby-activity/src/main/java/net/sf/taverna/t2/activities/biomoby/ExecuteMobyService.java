/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.sf.taverna.t2.activities.biomoby;

import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

/**
 * This class contains one method that is used to execute synchronous moby
 * services
 *
 * @author Edward Kawas
 *
 */

public class ExecuteMobyService {

	/**
	 *
	 * @param endpoint
	 *            the SOAP endpoint of the service to call
	 * @param service
	 *            the name of the service
	 * @param xml
	 *            the XML to send the service
	 * @return a string of XML representing the output from the service given
	 *         our input
	 * @throws MobyException
	 *             if anything goes wrong (SOAP error)
	 */
	public static String executeMobyService(String endpoint, String service,
			String xml) throws MobyException {
		return new CentralImpl(endpoint, "http://biomoby.org/").call(service,
				xml);
	}
}
