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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralDigestCachedImpl;
import org.biomoby.client.CentralImpl;
import org.biomoby.registry.meta.Registry;
import org.biomoby.shared.Central;
import org.biomoby.shared.MobyDataType;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyNamespace;

/**
 * A utility class that handles triggering JMoby to cache for a given {@link Registry} for a given registry
 *
 * @author Stuart Owen
 * @author Eddie Kawas
 */

public class BiomobyCache {

    private static Logger logger = Logger.getLogger(BiomobyCache.class);

	private static Map<String,Object> cached = new HashMap<String, Object>();

    /**
	 * If necessary caches the Ontology and namespace information.
     * This call immediately returns if the cache has been previously called for this endpoint
     *
	 * @param reg - the Registry instance
	 *
	 */
	public static synchronized void cacheForRegistry(Registry reg) {
		if (cached.get(reg.getEndpoint()) == null) {
			logger.info("Caching started for Biomoby registry"
					+ reg.getEndpoint());

			Central c;
			try {
				c = CentralImpl.getDefaultCentral(reg);
				if (c instanceof CentralDigestCachedImpl)
					((CentralDigestCachedImpl) c)
							.updateCache(CentralDigestCachedImpl.CACHE_PART_DATATYPES);
				MobyDataType.getDataType("Object", reg);
				MobyNamespace.getNamespace("foo", reg);

				cached.put(reg.getEndpoint(), new Boolean(true));
				logger.info("Caching complete for Biomoby registry"
						+ reg.getEndpoint());

			} catch (MobyException e) {
				logger.error("Error whilst caching for Biomoby registry",e);
			}

		}
	}

    /**
	 * If necessary caches the Ontology and namespace information.
     * This call immediately returns if the cache has been previously called for this endpoint url.
     *
	 * @param endpointUrl - the Registry endpoint Url
	 *
	 */
    public static synchronized void cacheForRegistryEndpoint(String endpointUrl) {
        Registry registry = new Registry(endpointUrl, endpointUrl,
					"http://domain.com/MOBY/Central");
        cacheForRegistry(registry);
    }

}
