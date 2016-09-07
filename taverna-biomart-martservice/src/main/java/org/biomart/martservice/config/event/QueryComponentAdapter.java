package org.biomart.martservice.config.event;
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

/**
 * An abstract adapter class for receiving <code>QueryComponent</code> events.
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 *
 * @author David Withers
 */
public class QueryComponentAdapter implements QueryComponentListener {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterChanged(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterChanged(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkAdded(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkRemoved(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.QueryComponentListener#linkChanged(org.biomart.martservice.config.QueryComponentEvent)
	 */
	public void linkChanged(QueryComponentEvent event) {
	}

}
